import styles from './CartContainer.module.css'
import { Link, useNavigate } from 'react-router-dom'
import { useState, useEffect, useRef } from 'react'

function Attribute({ id, name, values, selectedValue, handleSelectAttribute }) {
  return (
    <>
      <p>{name}:</p>
      <div>
        {values.map(value => (
          <span key={value}>
            <input type="radio" name={name} id={`item-${id}-${name}-${value}`} defaultChecked={selectedValue === value} onChange={() => handleSelectAttribute(name, value)} />
            <label htmlFor={`item-${id}-${name}-${value}`}>{value}</label>
          </span>
        ))}
      </div>
    </>
  )
}

function CartItem({ item, selectedAttributeTable, setSelectedAttributeTable, attributeTableRefs, setUpdate, handleCheckItem }) {
  var attributes = item?.variantAttributes
  const updateQuantity = (newQuantity) => {
    if (newQuantity < 0) newQuantity = 0
    else if (newQuantity >= 1000) newQuantity = 999
    const data = { quantity: newQuantity }
    fetch(`/api/cart/item/${item?.id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (response.ok) {
          setUpdate(pre => !pre)
        }
      })
  }

  const updateVariant = () => {
    const data = { id: item?.id, attributes: attributes }
    fetch(`/api/cart/item/${item?.id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
      .then(response => {
        if (response.ok) {
          setUpdate(pre => !pre)
          setSelectedAttributeTable(null)
        }
      })
  }

  const handleSelectAttribute = (name, value) => {
    attributes = { ...attributes, [name]: value }
  }
  return (
    <div className={styles.cartItem}>
      <div className={styles.check}>
        <input type="checkbox" onChange={() => handleCheckItem(item?.id)} />
      </div>
      <Link to={'/products/' + item?.productId} className={styles.poster}><img src={item.productImageUrl} /></Link>
      <div className={styles.name}><p>{item.productName}</p></div>
      <div className={styles.classification}>
        <button onClick={() => setSelectedAttributeTable(item.id)}>Phân loại</button>
        <div ref={(el) => {
          if (item?.id) {
            attributeTableRefs.current[item.id] = el
          }
        }}>
          {selectedAttributeTable == item.id && <div>
            <div className={styles.attributeTable}>
              {item?.productAttributes && Object.entries(item?.productAttributes).map(([name, values]) =>
                <Attribute key={"item-" + item?.id + "-" + name} id={item?.id} name={name} values={values} selectedValue={attributes[name]} handleSelectAttribute={handleSelectAttribute} />
              )}
            </div>
            {(item?.productAttributes && Object.keys(item?.productAttributes).length > 0) ? <button type="button" onClick={updateVariant}>Xác nhận</button> : <p>Không có phân loại</p>}
          </div>}
        </div>
        <div>
          {Object.keys(item?.variantAttributes).map(key => (
            <p key={key}>
              {key}: {item?.variantAttributes[key]}  </p>
          ))}
        </div>
      </div>
      <div className={styles.unit}>
        <p>{item.price.toLocaleString('de-DE')}đ</p>
      </div>
      <div className={styles.quantity}>
        <div>
          <img src="/decrease.png" onClick={() => updateQuantity(item.quantity - 1)} />
          <input type="text" value={item?.quantity} onChange={(e) => updateQuantity(e.target.value.replace(/[^0-9]/g, ''))} />
          <img src="/increase.png" onClick={() => updateQuantity(item.quantity + 1)} />
        </div>
      </div>
      <div className={styles.subTotal}><p>{(item.price * item.quantity).toLocaleString('de-DE')}đ</p></div>
    </div>
  )
}

function CartContainer() {
  const [cart, setCart] = useState(null)
  const [selectedAttributeTable, setSelectedAttributeTable] = useState(null)
  const attributeTableRefs = useRef({})
  const [checkedItems, setCheckedItems] = useState(new Set())
  const [update, setUpdate] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    fetch(`/api/cart`)
      .then(response => response.json())
      .then(data => setCart(data))
  }, [update])

  useEffect(() => {
    const handleHideAttributeTable = (e) => {
      if (selectedAttributeTable != null &&
        attributeTableRefs.current[selectedAttributeTable] &&
        !attributeTableRefs.current[selectedAttributeTable].contains(e.target)
      ) setSelectedAttributeTable(null)
    }
    document.addEventListener('mousedown', handleHideAttributeTable)
    return () => document.removeEventListener('mousedown', handleHideAttributeTable)
  }, [selectedAttributeTable])

  const handleCheckItem = (id) => {
    setCheckedItems(prev => {
      const newSet = new Set(prev)
      if (newSet.has(id)) {
        newSet.delete(id)
      } else {
        newSet.add(id)
      }
      return newSet

    })
  }

  const calculateTotal = () => {
    return cart?.reduce((total, cartItem) =>
      checkedItems.has(cartItem.id) ? total + cartItem.quantity * cartItem.price : total
      , 0) ?? 0
  }

  const handleDeleteCartItem = () => {
    const data = Array.from(checkedItems)
    const params = data.map(id => `ids=${id}`).join("&")
    fetch(`/api/cart/item?${params}`, {
      method: 'DELETE'
    })
      .then(response => {
        if (response.ok) {
          setUpdate(pre => !pre)
          setCheckedItems(new Set())
        }
      })
  }


  const handleSubmit = () => {
    sessionStorage.setItem("itemIds", JSON.stringify([...checkedItems]))
    navigate("/checkout")
  }

  return (
    <div className={styles.cartContainer}>
      {cart?.map(item =>
        <CartItem key={"item-" + item?.id} item={item} selectedAttributeTable={selectedAttributeTable} setSelectedAttributeTable={setSelectedAttributeTable} attributeTableRefs={attributeTableRefs} setUpdate={setUpdate} handleCheckItem={handleCheckItem} />
      )}

      <div className={styles.action}>
        <button onClick={handleDeleteCartItem}>Xóa ({checkedItems.size})</button>
        <div className={styles.order}>
          {/* dùng useMemo */}
          <div>Tổng tiền: {calculateTotal()?.toLocaleString('de-DE')}đ</div>
          <button onClick={() => handleSubmit()}>Xác nhận</button>
        </div>
      </div>
    </div >
  )
}

export default CartContainer