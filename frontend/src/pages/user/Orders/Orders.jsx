import styles from './Orders.module.css'
import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import User from '/src/pages/user/components/User/User.jsx'
import Pagination from '/src/components/Pagination/Pagination.jsx'
import { useEffect, useState } from 'react'
import { useSearchParams, Link} from 'react-router-dom'

function Orders() {
	const [data,setData]=useState()
	const [searchParams,setSearchParams]=useSearchParams()
	const status=searchParams.get('status')
	const page=+searchParams.get('page')
	const onPageChange=(newPage)=>{
		searchParams.set('page',newPage)
		setSearchParams(searchParams)
	}
	console.log(data)
	useEffect(()=>{
		// console.log(`/api/orders?status=${status}&page=${page}`)
		fetch(`/api/orders?status=${status}&page=${page}`)
		.then(response=>response.json())
		.then(data=>setData(data))
	},[searchParams])
	return (
		<>
			<Header />
			<User header="Thông tin đơn hàng">
				<nav className={styles.header}>					
					<Link className={`${styles.link} ${status==='confirmation-pending'&&styles.active}`} to='/orders?status=confirmation-pending&page=1'>Chờ xác nhận</Link>
					<Link className={`${styles.link} ${status==='preparing'&&styles.active}`} to='/orders?status=preparing&page=1'>Đang chuẩn bị</Link>
					<Link className={`${styles.link} ${status==='shipping'&&styles.active}`} to='/orders?status=shipping&page=1'>Đang giao hàng</Link>
					<Link className={`${styles.link} ${status==='completed'&&styles.active}`} to='/orders?status=completed&page=1'>Hoàn thành</Link>
					<Link className={`${styles.link} ${status==='failed'&&styles.active}`} to='/orders?status=failed&page=1'>Thất bại</Link>
				</nav>
				<div>
					{data?.orders?.map(order=>
					(<div>
						{order.id}
					</div>))}
				</div>
				<Pagination customStyle={styles.pagination} page={page} totalPages={data?.totalPages} onPageChange={onPageChange}/>
			</User>
			<Footer />
		</>
	)
}

export default Orders