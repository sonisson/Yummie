import styles from './Order.module.css'
import { useState, useEffect } from 'react'
import Toast from '../Toast/Toast.jsx'

function Quantity({ quantity, setQuantity }) {
  const changeQuantity = newQuantity => {
    if (newQuantity < 0) newQuantity = 0
    else if (newQuantity >= 1000) newQuantity = 999
    setQuantity(newQuantity)
  }

  return (
    <>
      <p>Số lượng:</p>
      <div className={styles.quantity}>
        <img onClick={() => changeQuantity(quantity - 1)} src="/decrease.png" />
        <input type="text" value={quantity} onChange={(e) => changeQuantity(e.target.value.replace(/[^0-9]/g, ''))} />
        <img onClick={() => changeQuantity(quantity + 1)} src="/increase.png" />
      </div>
    </>
  )
}


function Attribute({ handleSelectAttribute, name, values, selectedValue }) {
  return (
    <>
      <p>{name}:</p>
      <div>
        {values?.map(value => (
          <span key={`${name}-${value}`}>
            <input type="radio" name={name} id={`${name}-${value}`} checked={selectedValue === value} onChange={() => handleSelectAttribute(name, value)} />
            <label htmlFor={`${name}-${value}`}>{value}</label>
          </span>
        ))}
      </div>
    </>
  )
}


function Action({ variantId, quantity }) {
  const [showToast, setShowToast] = useState(false)

  const handleAddToCart = () => {
    const data = { variantId, quantity }
    fetch('/api/cart/item', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response=>{
        if(response.ok){
          setShowToast(true)
        }
      })
  }

  return (
    <>
      <div className={styles.action}>
        <button type="button" onClick={handleAddToCart}>
          <img src="/cart.png" />
          Thêm vào giỏ hàng
        </button>
        <button type="button">Đặt mua</button>
      </div>
      {showToast && <Toast message="Đã thêm vào giỏ hàng" duration={1000} onClose={() => setShowToast(false)} />}
    </>
  )
}

function Order({ data }) {
  const [quantity, setQuantity] = useState(1)
  const [variant, setVariant] = useState()

  useEffect(() => {
    setVariant(data?.variants[0])
  }, [data])

  const handleSelectAttribute = (name, value) => {
    const selectedAttributes = { ...variant?.attributes, [name]: value }
    setVariant(data.variants.find(variant => Object.entries(selectedAttributes)
      .every(([key, value]) => variant.attributes[key] === value
      )))
  }

  return (
    <div>
      <div className={styles.orderTable}>
        <Quantity quantity={quantity} setQuantity={setQuantity} />
        {data?.attributes && Object.entries(data?.attributes).map(([name, values]) => (
          <Attribute key={"attribute-" + name} name={name} values={values} handleSelectAttribute={handleSelectAttribute} selectedValue={variant?.attributes[name]} />
        ))}
      </div>
      <div className={styles.total}>{(quantity * (variant?.price ?? 0)).toLocaleString('de-DE') + "đ"}</div>
      <Action quantity={quantity} variantId={variant?.id} />
    </div>
  )
}

export default Order