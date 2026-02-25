import styles from './User.module.css'
import { NavLink } from 'react-router-dom'

function Sidebar() {
	return (
		<div className={styles.sideBar}>
			<div className={styles.avatar}>
				<img src="/avatar.png" />
			</div>
			<nav>
				<NavLink className={({isActive}) => `${styles.navLink} ${isActive && styles.active}` } to='/profile'>Hồ sơ của tôi</NavLink>
				<NavLink className={({isActive}) => `${styles.navLink} ${isActive && styles.active}` } to='/shipping-information'>Thông tin nhận hàng</NavLink>
				<NavLink className={({isActive}) => `${styles.navLink} ${isActive && styles.active}` } to='/orders?status=confirmation-pending&page=1'>Thông tin đơn hàng</NavLink>
				<NavLink className={({isActive}) => `${styles.navLink} ${isActive && styles.active}` } to='/password'>Đổi mật khẩu</NavLink>
				<NavLink className={({isActive}) => `${styles.navLink} ${isActive && styles.active}` } to='/logout'>Đăng xuất</NavLink>
			</nav>
		</div>
	)
}

function Container({header,children}){
	return(
		<div className={styles.container}>
			<div className={styles.header}>{header}</div>
			{children}
		</div>
	)
}

function User({header,children}){
	return(
		<div className={styles.user}>
			<Sidebar/>
			<Container header={header}>{children}</Container>
		</div>
	)
}

export default User;