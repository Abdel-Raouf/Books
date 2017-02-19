
#define DRIVER "eth0"

static struct net_device *rtl8139_dev;

//Structure which holds data private to our device and that structure shall be pointed by member  priv. 


#ifdef DUMMY

struct rtl8139_private {
        struct pci_dev *pci_dev;  /* PCI device */
        void *mmio_addr; /* memory mapped IO addr */
        unsigned long regs_len; /* length of IO or MMIO region */
	struct net_device_stats stats;
};

#else
#define NUM_TX_DESC 4
struct rtl8139_private {
        struct pci_dev *pci_dev;  /* PCI device */
        void *mmio_addr; /* memory mapped IO addr */
        unsigned long regs_len; /* length of IO or MMIO region */
        unsigned int tx_flag;
        unsigned int cur_tx;
        unsigned int dirty_tx;
        unsigned char *tx_buf[NUM_TX_DESC];     /* Tx bounce buffers */
        unsigned char *tx_bufs; /* Tx bounce buffer region. */
        dma_addr_t tx_bufs_dma;
	struct net_device_stats stats;
        unsigned char *rx_ring;
        dma_addr_t rx_ring_dma;
        unsigned int cur_rx;
};

#define TX_BUF_SIZE  1536  /* should be atleast MTU + 14 + 4 */
#define TOTAL_TX_BUF_SIZE  (TX_BUF_SIZE * NUM_TX_DESC)

/* 8139 register offsets */
#define TSD0          0x10
#define TSAD0       0x20
#define RBSTART  0x30
#define CR               0x37
#define CAPR         0x38
#define IMR            0x3c
#define ISR             0x3e
#define TCR           0x40
#define RCR           0x44
#define MPC           0x4c
#define MULINT    0x5c

/* TSD register commands */
#define TxHostOwns    0x2000
#define TxUnderrun    0x4000
#define TxStatOK      0x8000
#define TxOutOfWindow 0x20000000
#define TxAborted     0x40000000
#define TxCarrierLost 0x80000000

#define RX_BUF_LEN_IDX  2         /* 0==8K, 1==16K, 2==32K, 3==64K */
#define RX_BUF_LEN      (8192 << RX_BUF_LEN_IDX)
#define RX_BUF_PAD      16           /* see 11th and 12th bit of RCR: 0x44 */
#define RX_BUF_WRAP_PAD 2048   /* spare padding to handle pkt wrap */
#define RX_BUF_TOT_LEN   \
(RX_BUF_LEN + RX_BUF_PAD + RX_BUF_WRAP_PAD)



/* CR register commands */
#define RxBufEmpty 0x01
#define CmdTxEnb   0x04
#define CmdRxEnb   0x08
#define CmdReset   0x10

/* ISR Bits */
#define RxOK       0x01
#define RxErr      0x02
#define TxOK       0x04
#define TxErr      0x08
#define RxOverFlow 0x10
#define RxUnderrun 0x20
#define RxFIFOOver 0x40
#define CableLen   0x2000
#define TimeOut    0x4000
#define SysErr     0x8000

#define INT_MASK (RxOK | RxErr | TxOK | TxErr | \
                RxOverFlow | RxUnderrun | RxFIFOOver | \
                CableLen | TimeOut | SysErr)

void rtl8139_hw_start(struct net_device *);
void rtl8139_chip_reset(void *);
void rtl8139_init_ring(struct net_device *);    
#endif

