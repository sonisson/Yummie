import styles from './Product.module.css'
import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import Detail from './Detail/Detail.jsx'
import Order from './Order/Order.jsx'
import Comment from './Comment/Comment.jsx'
import Recommendation from './Recommendation/Recommendation.jsx'
import { useParams} from 'react-router-dom'
import { useState, useEffect} from 'react'


function Main() {
  const { id } = useParams()
	const [data,setData]=useState()
	useEffect(() => {
		fetch(`/api/products/${id}`)
			.then(response => response.json())
			.then(data => setData(data))
	}, []);
  return (
    <div className={styles.main}>
      <div className={styles.container}>
        <img src={data?.imageUrl} />
        <Detail data={data}/>
        <Order data={data}/>
        <Comment productId={id}/>
        <Recommendation/>
      </div>
    </div>

  )
}

function Product() {
  return (
    <>
      <Header />
      <Main />
      <Footer />
    </>
  )
}

export default Product