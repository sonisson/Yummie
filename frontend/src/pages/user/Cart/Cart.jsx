import styles from './Cart.module.css'
import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import CartContainer from './CartContainer/CartContainer.jsx'
import Recommendation from './Recommendation/Recommendation.jsx'

function Cart() {
  return (
    <>
      <Header />
      <div className={styles.main}>
        <div className={styles.container}>
          <CartContainer />
          <Recommendation />
        </div>
      </div >
      <Footer />
    </>

  )
}

export default Cart;