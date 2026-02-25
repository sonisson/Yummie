import styles from './Comment.module.css'
import Pagination from '/src/components/Pagination/Pagination.jsx'
import { useState, useEffect } from 'react'
import dayjs from 'dayjs';

function Filter({ star, onStarChange, time, onTimeChange, reviewSummary }) {
	const Star = ({ label, count, children }) => {
		return (
			<>
				<input name="star" type="radio" id={"star-" + label} checked={star === label} onChange={() => onStarChange(label)} />
				<label htmlFor={"star-" + label} >{children} {!(label === "-1") && <img className={styles.starIcon} src="/star.png" />} ({count})</label>
			</>
		)
	}
	const Time = ({ label, children }) => {
		return (
			<>
				<input type="radio" name="time" id={"time-" + label} checked={time === label} onChange={() => onTimeChange(label)} />
				<label htmlFor={"time-" + label}>{children}</label>
			</>
		)
	}

	return (
		<>
			<div className={styles.starFilter}>
				<Star label="-1" count={reviewSummary?.total}>Tất cả</Star>
				<Star label="0" count={reviewSummary?.rating0}>0</Star>
				<Star label="1" count={reviewSummary?.rating1}>1</Star>
				<Star label="2" count={reviewSummary?.rating2}>2</Star>
				<Star label="3" count={reviewSummary?.rating3}>3</Star>
				<Star label="4" count={reviewSummary?.rating4}>4</Star>
				<Star label="5" count={reviewSummary?.rating5}>5</Star>
			</div>
			<div className={styles.timeFilter}>
				<Time label="newest">Mới nhất</Time>
				<Time label="oldest">Cũ nhất</Time>
			</div>
		</>
	)
}

function Rate({ rating }) {
	return (
		<div>
			{Array.from({ length: 5 }).map((_, index) => (
				<img className={styles.starIcon}
					key={index}
					src={index < rating ? '/star.png' : '/empty-star.png'}
				/>
			))}
		</div>
	)
}

function CommentList({ page, totalPages, onPageChange, reviews}) {
	return (
		<>
			<div>
				{reviews?.map((review) => (
					<div key={"review-" + review.id} className={styles.commentItem}>
						<img src="/avatar.png" />
						<div>
							<p className={styles.username}>{review.userEmail}</p>
							<p className={styles.content}>{review.comment}</p>
							<div className={styles.rate}>
								<Rate rating={review.rating} />
								<p>{dayjs(review.createAt).format("HH:mm:ss DD/MM/YYYY")}</p>
							</div>
						</div>
					</div>
				))}
			</div>
			<Pagination page={page} totalPages={totalPages} onPageChange={onPageChange} customStyle={styles.pagination} />
		</>
	)
}

function Comment({ productId }) {
	const [reviewSummary, setReviewSummary] = useState();
	const [data, setData] = useState();
	const [star, setStar] = useState("-1")
	const [time, setTime] = useState("newest")
	const [page, setPage] = useState(1);

	const onStarChange = (newStar) => {
		setStar(newStar)
		setPage(1)
	};
	const onTimeChange = (newTime) => {
		setTime(newTime)
		setPage(1)
	};

	useEffect(() => {
		fetch(`/api/reviews/summary?product-id=${productId}`)
			.then(response => response.json())
			.then(data => setReviewSummary(data))
	}, []);

	useEffect(() => {
		fetch(`/api/reviews?product-id=${productId}&star=${star}&time=${time}&page=${page}`)
			.then(response => response.json())
			.then(data => setData(data))
	}, [star, time, page]);

	return (
		<div className={styles.comment}>
			<p>Đánh giá ({reviewSummary?.total})</p>
			<Filter star={star} onStarChange={onStarChange} time={time} onTimeChange={onTimeChange} reviewSummary={reviewSummary} />
			<CommentList page={data?.page} totalPages={data?.totalPages} onPageChange={setPage} reviews={data?.data}></CommentList>
		</div>
	)
}

export default Comment;