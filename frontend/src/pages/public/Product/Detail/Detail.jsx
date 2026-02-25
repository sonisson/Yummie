import styles from './Detail.module.css'

function Detail({ data }) {
	return (
		<div>
			<p className={styles.name}>{data?.name}</p>
			<p className={styles.price}>20.000đ - 25.000đ</p>
			<div className={styles.description}>
				<p className={styles.key}>Hương vị:</p>
				<p className={styles.value}>ngọt nhẹ, béo min, thanh mát</p>
				<p className={styles.key}>Topping:</p>
				<p className={styles.value}>trân châu đen nấu đường</p>
				<p className={styles.key}>Thành phần:</p>
				<p className={styles.value}>sữa tươi, chân trâu đen, đường, đá</p>
				<p className={styles.key}>Đã bán:</p>
				<p className={styles.value}>214</p>
				<p className={styles.key}>Đánh giá:</p>
				{data?.rating ? <p className={styles.value}>{data?.rating.toFixed(1)}/5</p> : <p className={styles.value}>Chưa có đánh giá</p>}
			</div>
		</div>
	)
}

export default Detail