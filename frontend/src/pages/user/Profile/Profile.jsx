import styles from './Profile.module.css'
import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import User from '/src/pages/user/components/User/User.jsx'
import { useEffect, useState } from 'react'

function ConfirmModal({ profile, setShowConfirm, image }) {

	const handleUpdate = () => {
		const formData = new FormData();
		if (image != null) formData.append("image", image);
		formData.append('name', profile.name)
		formData.append("tel", profile.tel);
		formData.append('birthDate', profile.birthDate)
		formData.append("gender", profile.gender)

		fetch("/api/profile", {
			method: "PUT",
			body: formData
		})
			.then(() => setShowConfirm(false))
	}
	return (
		<div className={styles.confirmOverlay}>
			<div className={styles.confirmContent}>
				<p>Lưu thay đổi?</p>
				<section>
					<button type="button" onClick={() => handleUpdate()}>Lưu</button>
					<button type="button" onClick={() => setShowConfirm(false)}>Hủy</button>
				</section>
			</div>
		</div>
	)
}

function Profile() {

	const [showConfirm, setShowConfirm] = useState(false)
	const [profile, setProfile] = useState()
	const [image, setImage] = useState()

	useEffect(() => {
		fetch('/api/profile')
			.then(response => response.json())
			.then(data => setProfile(data))
	}, [])

	return (
		<>
			<Header />
			{showConfirm && <ConfirmModal profile={profile} setShowConfirm={setShowConfirm} image={image} />}
			<User header='Hồ sơ của tôi'>
				<form className={styles.form}>
					<fieldset>
						<div>
							<label htmlFor='name'>Họ và tên</label>
							<input type='text' id='name' value={profile?.name ?? ''} onChange={e => setProfile({ ...profile, name: e.target.value })} />
						</div>
						<div>
							<label htmlFor='tel'>Số điện thoại</label>
							<input type='tel' id='tel' value={profile?.tel ?? ''} onChange={e => setProfile({ ...profile, tel: e.target.value })} />
						</div>
						<div>
							<label htmlFor='email'>Email</label>
							<input type='email' id='email' value={profile?.email ?? ''} readOnly />
						</div>
						<div>
							<label htmlFor='birthDate'>Ngày sinh</label>
							<input type="date" id='birthDate' value={profile?.birthDate ?? ''} onChange={e => setProfile({ ...profile, birthDate: e.target.value })} />
						</div>
						<div>
							<label>Giới tính</label>
							<input id='male' type='radio' name='gender' value='Nam' checked={profile?.gender == 'Nam'} onChange={e => setProfile({ ...profile, gender: e.target.value })} />
							<label htmlFor='male'>Nam</label>
							<input id='female' type='radio' name='gender' value='Nữ' checked={profile?.gender == 'Nữ'} onChange={e => setProfile({ ...profile, gender: e.target.value })} />
							<label htmlFor='female'>Nữ</label>
							<input id='other' type='radio' name='gender' value='Khác' checked={profile?.gender == 'Khác'} onChange={e => setProfile({ ...profile, gender: e.target.value })} />
							<label htmlFor='other'>Khác</label>
						</div>
						<button type='button' onClick={() => setShowConfirm(true)}>Lưu thay đổi</button>
					</fieldset>
					<div className={styles.avatar}>
						<img src={image ? URL.createObjectURL(image) : profile?.imageUrl} />
						<label htmlFor='avatar'>Chọn ảnh</label>
						<input id='avatar' type="file" onChange={e => setImage(e.target.files[0])} />
					</div>
				</form>
			</User>
			<Footer />
		</>
	)
}

export default Profile;