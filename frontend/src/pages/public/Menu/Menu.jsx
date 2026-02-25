import Header from '/src/pages/user/components/Header/Header.jsx'
import Footer from '/src/components/Footer/Footer.jsx'
import styles from './Menu.module.css'
import { useSearchParams, Link, useLocation } from 'react-router-dom'
import Pagination from '/src/components/Pagination/Pagination.jsx'
import Product from '/src/components/Product/Product.jsx'
import { useEffect, useState } from 'react'
function Banner() {
  return (
    <div className={styles.promotion}>
      <img className={styles.slider} src="/previous.png" />
      <img className={styles.banner} src="/banner.jpg" />
      <img className={styles.slider} src="/next.png" />
    </div>
  )
}
function Filter({ currentFilter }) {
  const location = useLocation();
  const FilterItem = ({ filter, children }) => {
    return (
      <Link to={`${location.pathname}?filter=${filter}&page=1`}
        className={`${currentFilter == filter ? styles.active : ''}`}
      >
        {children}
      </Link>
    )
  }
  return (
    <div className={styles.filter}>
      <FilterItem filter="top">Top</FilterItem>
      <FilterItem filter="popular">Phổ biến</FilterItem>
      <FilterItem filter="newest">Mới nhất</FilterItem>
      <FilterItem filter="discount">Khuyến mại</FilterItem>
      <FilterItem filter="low">Giá thấp</FilterItem>
      <FilterItem filter="high">Giá cao</FilterItem>
    </div>
  );
}

function Main() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [data, setData] = useState();
  const location = useLocation();
  const onPageChange = (newPage) => {
    searchParams.set('page', newPage)
    setSearchParams(searchParams)
  };
  useEffect(() => {
    fetch(`/api/products/category${location.pathname}${location.search}`)
      .then(response =>response.json())
      .then(data => setData(data))
  }, [location]);
  return (
    <div className={styles.main}>
      <div className={styles.container}>
        <Filter currentFilter={searchParams.get('filter')} />
        <div className={styles.pContainer}>
          {data?.data?.map(product => (
            <Product key={`product-${product?.id}`} customStyle={styles.product} data={product} />
          ))}
        </div>
        <Pagination customStyle={styles.pagination} page={data?.page} totalPages={data?.totalPages} onPageChange={onPageChange} />
      </div>
    </div>
  )
}

function Menu() {
  return (
    <>
      <Header />
      <Banner />
      <Main />
      <Footer />
    </>
  )
}

export default Menu;