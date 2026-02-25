import styles from '/src/components/Footer/Footer.module.css'

function Footer(){
  return(
    <footer className={styles.footer}>
      <h2>YUMMIE YUMMIE</h2>
      <p>Địa chỉ: Km10, đường Nguyễn Trãi, Q Hà Đông, TP Hà Nội</p>
      <p>SĐT: 0389 290 231</p>
      <p>Email: leson121204@gmail.com</p>
      <p>Page: facebook.com </p>
    </footer>
  );
}

export default Footer;