import styles from './Product.module.css'
import { Link } from 'react-router-dom';

function Product({customStyle,data}) {
	// return (
	// 	<Link className={`${customStyle} ${styles.product}`} to={`/product/${data?.id}`}>
	// 		<img src={data?.imageUrl} />
	// 		<p>{data?.name}</p>
	// 		<p>20.000 - 25.000</p>
	// 		<p>Đã bán: 214</p>
	// 		{data?.rating==null?<p>Chưa có đánh giá</p>:<p>Rating: {data?.rating.toFixed(1)}/10</p>}
	// 	</Link>
	// )
	return (
		<a className={`${customStyle} ${styles.product}`} href={`/products/${data?.id}`}>
			<img src={data?.imageUrl} />
			<p>{data?.name}</p>
			<p>20.000 - 25.000</p>
			<p>Đã bán: 214</p>
			{data?.rating==null?<p>Chưa có đánh giá</p>:<p>Rating: {data?.rating.toFixed(1)}/10</p>}
		</a>
	)
}

// function Product2() {
//   return (
//     <Link className={styles.product} to="/product/1">
//       <img src="/product.jpg" className={styles.img} />
//       <p className={styles.name}>Sữa tươi trân g 5wg wg 3g 5g wgr we5g w5g w5g ư5gchâu đường đen</p>
//       <p className={styles.price}>20.000 - 25.000</p>
//       <p className={styles.sold}>Đã bán: 214</p>
//       <p className={styles.rate}>Đánh giá: 8.5/10</p>
//     </Link>
//   )
// }

export default Product;