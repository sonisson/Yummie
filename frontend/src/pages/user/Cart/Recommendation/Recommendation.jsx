import styles from './Recommendation.module.css'
import Product from '/src/components/Product/Product.jsx'
import Pagination from '/src/components/Pagination/Pagination.jsx'
import { useState } from 'react';

function Recommendation() {
  const [page,setPage]=useState(1)
  const onPageChange = (newPage) => {
		setPage(newPage)
	}
  return (
    <div className={styles.recommendation}>
      <p>Xem thêm</p>
      <div className={styles.pContainer}>
        <Product customStyle={styles.product} />
        <Product customStyle={styles.product} />
        <Product customStyle={styles.product} />
        <Product customStyle={styles.product} />
        <Product customStyle={styles.product} />
      </div>
      <Pagination page={page} pageSize={500} onPageChange={onPageChange} customStyle={styles.pagination}/>
    </div>
  )
}

export default Recommendation;