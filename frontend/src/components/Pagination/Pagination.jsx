import styles from './Pagination.module.css'

function Pagination({customStyle,page, totalPages, onPageChange}) {
  return (
    <>
      {totalPages>1 && <div className={`${styles.pagination} ${customStyle}`}>
        {page>1 && <img  src="/previous.png" onClick={() => onPageChange(page - 1)} />}
        {page >= 3 && <span>...</span>}
        {page >= 2 && <div className={styles.otherPage} onClick={() => onPageChange(page - 1)}>{page - 1}</div>}
        <div className={styles.currentPage} >{page}</div>
        {totalPages - page >= 1 && <div className={styles.otherPage}  onClick={() => onPageChange(page + 1)}>{page + 1}</div>}
        {totalPages - page >= 2 && <span>...</span>}
        {page<totalPages && <img src="/next.png" onClick={() => onPageChange(page + 1)} />}
      </div>}
    </>
  )
}
export default Pagination;