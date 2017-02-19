package hirondelle.web4j.config;

import java.util.Locale;
import hirondelle.web4j.ui.translate.Translator;

public class TranslatorImpl implements Translator {

  /** Simply return the base text as is. */
  public String get(String aBaseText, Locale aLocale) {
    return aBaseText;
  }

}
