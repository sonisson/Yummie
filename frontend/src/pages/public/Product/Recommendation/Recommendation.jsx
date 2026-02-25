import styles from './Recommendation.module.css'
import Product from '/src/components/Product/Product.jsx'
import Pagination from '/src/components/Pagination/Pagination.jsx'
import { useState, useRef } from 'react'

function Recommendation() {
	const ref = useRef(null);
	if (ref.current) {
		const offset = 100;
		const top = ref.current.getBoundingClientRect().top + window.scrollY;
		window.scrollTo({
			top: top - offset,
			behavior: "smooth"
		})
	}
	const [page, setPage] = useState(1)
	const onPageChange = (newPage) => {
		setPage(newPage)
	}
	return (
		// <div ref={ref} className={styles.recommendationContainer}>
		<div className={styles.recommendationContainer}>
			<p>Xem thêm</p>
			<div className={styles.container}>
				<Product customStyle={styles.product} />
				<Product customStyle={styles.product} />
				<Product customStyle={styles.product} />
				<Product customStyle={styles.product} />
			</div>
			<Pagination page={page} pageSize={5} onPageChange={onPageChange} customStyle={styles.pagination} />
		</div>
	)
}

export default Recommendation;