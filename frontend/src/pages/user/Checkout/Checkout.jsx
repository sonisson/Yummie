import styles from './Checkout.module.css'
import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import { useEffect, useState, useMemo } from 'react'


function Invoice({itemIds}){

  const [order, setOrder]=useState()
  var shippingFee = 0

  useEffect(()=>{
    const params = itemIds.map(id => `ids=${id}`).join("&")
    fetch(`/api/orders/preview?${params}`)
    .then(response=>response.json())
    .then(data=>{setOrder(data)})
  },[])

  const itemsTotal = useMemo(() => {
    return order?.reduce((itemsTotal, orderItem) => itemsTotal + orderItem.price * orderItem.quantity,0
    ) ?? 0;
  }, [order]);

  const handleCheckout=()=>{
    fetch('/api/orders',{
      method:'POST',
      headers:{
        'Content-Type':'application/json'
      },
      body:JSON.stringify(itemIds)
    })
    .then(response=>response.json())
    .then(data=>{
      fetch('/api/payment',{
        method:'POST',
        headers:{
          'Content-Type':'application/json'
        },
        body:JSON.stringify({orderId:data?.id})
      })
      .then(response=>response.json())
      .then(data=>window.location.href = data.url)
    })
  }

  return(
    <div className={styles.invoice}>
      {order?.map((orderItem,index)=>
        <div key={'order-item-'+orderItem?.id} className={styles.invoiceItem}>
          <p className={styles.no}>{index+1}</p>
          <img className ={styles.img} src={orderItem.productImageUrl}/>
          <p className={styles.name}>{orderItem.productName}</p>
          <div className={styles.classification}>
            {orderItem?.variantAttributes
              && Object.entries(orderItem?.variantAttributes).map(([name,value])=>
              <p key={'attribute-'+name}>{name}: {value}</p>)
            }
          </div>
          <p className={styles.unit}>{orderItem?.price?.toLocaleString('de-DE')}</p>
          <p className={styles.quantity}>{orderItem?.quantity}</p>
          <p className={styles.subTotal}>{(orderItem?.price*orderItem?.quantity).toLocaleString('de-DE')}</p>
        </div>
      )}
      <div className={styles.summary}>
        <p className={styles.label}>Tiền hàng:</p>
        <p className={styles.price}>{itemsTotal.toLocaleString('de-DE')}đ</p>
        <p className={styles.label}>Phí giao hàng:</p>
        <p className={styles.price}>{shippingFee.toLocaleString('de-DE')}đ</p>
        <p className={styles.label}>Tổng cộng:</p>
        <p className={styles.price}>{(itemsTotal+shippingFee).toLocaleString('de-DE')}đ</p>
        <button onClick={handleCheckout}>
          Đặt hàng ({(itemsTotal+shippingFee).toLocaleString('de-DE')}đ)
        </button>
      </div>
    </div>
  )
}

function ShippingInfor(){
  const [shippingInfor,setShippingInfor]=useState();

  useEffect(() => {
  fetch('/api/delivery-info/default')
    .then(response => {
      if (response.ok) {
        return response.json();
      }
    })
    .then(data => setShippingInfor(data))
}, []);


  return(
    <div className={styles.shippingInfor}>
      <h4>Thông tin nhận hàng:</h4>
      <p>{shippingInfor?.name}</p>
      <p>{shippingInfor?.tel}</p>
      <p>{shippingInfor?.address}</p>
      <p className={styles.change}>Thay đổi</p>
      <h4>Phương thức thanh toán:</h4>
      <div className={styles.payment}>
        <button>Chuyển khoản trước</button>
        <button>Thanh toán khi nhận hàng</button>
      </div>
      <h4>Ghi chú (nếu có):</h4>
      <textarea></textarea>
    </div>
  )
}

function Main() {
  const itemIds=JSON.parse(sessionStorage.getItem("itemIds"))
  return (
    <div className={styles.main}>
      <div className={styles.container}>
        <Invoice itemIds={itemIds} />
        <ShippingInfor/>
      </div>
    </div>
  )
}

function Checkout() {
  return (
    <>
      <Header />
      <Main />
      <Footer />
    </>
  )
}

export default Checkout