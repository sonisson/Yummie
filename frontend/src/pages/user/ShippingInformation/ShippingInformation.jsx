import styles from './ShippingInformation.module.css'
import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import User from '/src/pages/user/components/User/User.jsx'
import { useEffect, useState, useMemo } from 'react'
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'

function Form({ item, setSelectedItem, setReload }) {

	const [provinces, setProvinces] = useState([])
	const [wards, setWards] = useState([])
	const [data, setData] = useState(item);

	useEffect(() => {
		fetch('/provinces.json')
			.then(response => response.json())
			.then(data => setProvinces(data))

		fetch('/wards.json')
			.then(response => response.json())
			.then(data => setWards(data))

	}, [])

	const handleSubmit = async () => {
		if (data.id != null) {
			await fetch(`api/delivery-info/${data.id}`, {
				method: 'PUT',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(data)
			})
		}
		else {
			await fetch('api/delivery-info', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(data)
			})
		}
		setSelectedItem(null)
		setReload(pre => !pre)
	}

	const wardsInProvince = useMemo(() => {
		return wards.filter(ward => ward.province == data?.province);
	}, [data, wards]);

	function MapClickHandler() {
		useMapEvents({
			click(e) {
				setData({ ...data, latitude: e.latlng.lat, longitude: e.latlng.lng })
			},
		})
		return null
	}

	return (
		<div className={styles.formOverlay}>
			<div className={styles.formContent}>
				<header>Cập nhật thông tin</header>
				<form>
					<div className={styles.name}>
						<label htmlFor='name'>Họ và tên</label>
						<input type='text' id="name" value={data?.name} onChange={e => setData({ ...data, name: e.target.value })} />
					</div>
					<div className={styles.tel}>
						<label htmlFor='tel'>Số điện thoại</label>
						<input type='tel' id='tel' value={data?.tel} onChange={e => setData({ ...data, tel: e.target.value })} />
					</div>
					<div className={styles.province}>
						<label>Chọn tỉnh/thành</label>
						<select value={data?.province} onChange={e => setData({ ...data, province: e.target.value })}>
							<option value="">Chọn tỉnh/thành</option>
							{provinces?.map(p => (
								<option key={'province-' + p.name} value={p.name}>{p.name}</option>
							))}
						</select>
					</div>
					<div className={styles.ward}>
						<label>Chọn phường/xã</label>
						<select value={data?.ward} onChange={e => setData({ ...data, ward: e.target.value })}>
							<option value="">Chọn phường/xã</option>
							{wardsInProvince?.map(w => (
								<option key={'ward-' + w.name} value={w.name}>{w.name}</option>
							))}
						</select>
					</div>
					<div className={styles.address}>
						<label>Địa chỉ chi tiết</label>
						<input type='text' value={data?.detailAddress} onChange={e => setData({ ...data, detailAddress: e.target.value })} />
					</div>
					<div className={styles.default}>
						<input id='default' type='checkbox' checked={data?.default} onChange={e => setData({ ...data, default: e.target.checked })} />
						<label htmlFor='default'>Đặt làm mặc định</label>
					</div>
					<MapContainer className={styles.map} center={[data?.latitude, data?.longitude]} zoom={13}>
						<TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
						<Marker position={[data?.latitude ?? 21.0285, data?.longitude ?? 105.8542]}></Marker>
						<MapClickHandler />
					</MapContainer>
					<div className={styles.formAction}>
						<button type='button' onClick={handleSubmit}>Lưu thay đổi</button>
						<button type="button" onClick={() => setSelectedItem(null)}>Hủy</button>
					</div>
				</form>
			</div>
		</div>
	)
}

function DeleteModal({ id, setDeleteId, setReload }) {
	const handleDelete = id => {
		fetch(`api/delivery-info/${id}`, {
			method: 'DELETE'
		})
			.then(() => {
				setDeleteId(null)
				setReload(pre => !pre)
			}
			)
	}
	return (
		<div className={styles.deleteOverlay}>
			<div className={styles.deleteContent}>
				<p>Bạn muốn xóa địa chỉ này?</p>
				<section>
					<button onClick={() => handleDelete(id)}>Xác nhận</button>
					<button onClick={() => setDeleteId(null)}>Hủy</button>
				</section>
			</div>
		</div>
	)
}

function ShippingInformation() {
	const [data, setData] = useState()
	const [selectedItem, setSelectedItem] = useState(null)
	const [reload, setReload] = useState(false)
	const [deleteId, setDeleteId] = useState(null)
	useEffect(() => {
		fetch('/api/delivery-info')
			.then(response => response.json())
			.then(data => setData(data))
	}, [reload])

	return (
		<>
			<Header />
			{selectedItem != null && <Form item={selectedItem} setSelectedItem={setSelectedItem} setReload={setReload} />}
			{deleteId != null && <DeleteModal id={deleteId} setDeleteId={setDeleteId} setReload={setReload} />}
			<User header="Thông tin nhận hàng">
				<div className={styles.container}>
					<div>
						{data?.map(item => (
							<div key={'shipping-information-' + item.id} className={styles.item}>
								<p>{item.name}</p>
								<p>{item.tel}</p>
								<p>{item.detailAddress + ', ' + item.ward + ', ' + item.province}</p>
								<div className={styles.action}>
									<p onClick={() => setSelectedItem(item)}>Sửa</p>
									<p onClick={() => setDeleteId(item.id)}>Xóa</p>
									{item.default && <p>Mặc định</p>}
								</div>
							</div>
						))}
					</div>
					<button onClick={() => setSelectedItem({longitude:105.8542,latitude:21.0285})}>Tạo mới</button>
				</div>
			</User>
			<Footer />
		</>
	)
}

export default ShippingInformation