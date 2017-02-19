#include <linux/module.h>
#include <linux/vermagic.h>
#include <linux/compiler.h>

MODULE_INFO(vermagic, VERMAGIC_STRING);

#undef unix
struct module __this_module
__attribute__((section(".gnu.linkonce.this_module"))) = {
 .name = __stringify(KBUILD_MODNAME),
 .init = init_module,
#ifdef CONFIG_MODULE_UNLOAD
 .exit = cleanup_module,
#endif
};

static const struct modversion_info ____versions[]
__attribute_used__
__attribute__((section("__versions"))) = {
	{ 0x6d134bc3, "struct_module" },
	{ 0x383f55ea, "pci_disable_device" },
	{ 0xff0eb352, "unregister_netdev" },
	{ 0x2c916f04, "pci_release_regions" },
	{ 0xedc03953, "iounmap" },
	{ 0x4a13e124, "register_netdev" },
	{ 0x9eac042a, "__ioremap" },
	{ 0x21014b62, "pci_set_master" },
	{ 0x938bbbc4, "pci_request_regions" },
	{ 0x2455109f, "pci_enable_device" },
	{ 0x37cd782, "pci_find_device" },
	{ 0x778234c7, "alloc_etherdev" },
	{ 0x1b7d4074, "printk" },
};

static const char __module_depends[]
__attribute_used__
__attribute__((section(".modinfo"))) =
"depends=";


MODULE_INFO(srcversion, "B63F36EE77707C928813E62");
