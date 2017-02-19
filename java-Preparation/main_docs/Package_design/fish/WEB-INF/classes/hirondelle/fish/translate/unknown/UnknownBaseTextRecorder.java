package hirondelle.fish.translate.unknown;

import hirondelle.web4j.model.AppException;
import hirondelle.web4j.action.ActionImpl;
import hirondelle.web4j.request.RequestParser;
import hirondelle.web4j.request.RequestParameter;
import hirondelle.web4j.action.Operation;
import hirondelle.web4j.config.TranslatorImpl;
import hirondelle.web4j.action.ResponsePage;
import hirondelle.web4j.ui.translate.Translator;
import hirondelle.fish.translate.basetext.BaseText;

/**
 Toggle the recording of unknown {@link BaseText}. 
 
 <P>See {@link Translator} for a definition of <tt>BaseText</tt>.
 
 <P>When a particular text snippet is first encountered by various translation tools,
 it is categorized as "unknown" <tt>BaseText</tt>. Such unknown items can be 
 collected by calling this <tt>Action</tt>, which will start to 'record' such items 
 as they are found. Later, the items are evaluated one by one, to determine if they 
 should be treated as translatable items. 
 
 <P>Operations performed :{@link TranslatorImpl#startRecordingUnknowns()} and 
 {@link TranslatorImpl#stopRecordingUnknowns()}.
 
 @view view.jsp
*/
public final class UnknownBaseTextRecorder extends ActionImpl {

  /** Constructor.  */
  public UnknownBaseTextRecorder(RequestParser aRequestParser){
    super(UnknownBaseTextEdit.REDIRECT_TO_LISTING, aRequestParser);
  }
  
  /**
   {@link RequestParameter} defining the operations supported by this template.
   
   <P>The underlying <tt>request</tt> must contain a parameter 
   named <tt>'Operation'</tt>, whose single value can be parsed successfully 
   by {@link Operation#valueOf} into one of the following supported operations : 
   <ul>
   <li>{@link Operation#Start}
   <li>{@link Operation#Stop}
   </ul>
  */
  public static final RequestParameter SupportedOperation = RequestParameter.withRegexCheck(
    "Operation", "(" + Operation.Start + "|" + Operation.Stop + ")"
  );
   
  /**
   Toggle the recording of unknown <tt>BaseText</tt>, according to the value of 
   {@link #SupportedOperation}.
   
   <P>Uses {@link TranslatorImpl#startRecordingUnknowns()} and 
   {@link TranslatorImpl#stopRecordingUnknowns()}.
  */
  public ResponsePage execute() throws AppException {
    Operation operation = Operation.valueOf(getParamUnsafe(SupportedOperation));
    if ( Operation.Start == operation ) {
      if( ! TranslatorImpl.isRecording() ){
        TranslatorImpl.read();//refreshes the in-memory cache
        TranslatorImpl.startRecordingUnknowns();
        addMessage("Started recording.");
      }
      else {
        addError("Already recording Unknown Base Text.");
      }
    }
    else if( Operation.Stop == operation ){
      if( TranslatorImpl.isRecording() ) {
        TranslatorImpl.stopRecordingUnknowns();
        addMessage("Stopped recording.");
      }
      else {
        addError("Not recording Unknown Base Text.");
      }
    }
    return getResponsePage();
  }
}
