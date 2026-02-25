import styles from './Header.module.css'
import { NavLink, Link} from 'react-router-dom';

function NavLogo() {
  return (
    <Link to="/menu?filter=top&page=1">
      <img src="/logo.png" className={styles.img}/>
    </Link>
  )
}


function NavMenu() {
  const NavItem=({to,children})=>{
    return(
      <NavLink
        to={`${to}?filter=top&page=1`}
        className={({ isActive }) => `${styles.navItem} ${isActive ? styles.active : ''}`}
      >
        {children}
      </NavLink>
    )
  }
  return (
    <>
      <NavItem to="/menu">MENU</NavItem>
      <NavItem to="/drink">DRINK</NavItem>
      <NavItem to="/food">FOOD</NavItem>
      <NavItem to="/combo">COMBO</NavItem>
    </>
  )
}

function NavIcons() {
  return (
    <>
      <Link to="/cart">
        <img className={styles.img} src="/cart.png" />
      </Link>
      <Link to="/profile">
        <img className={styles.img} src="/avatar.png"/>
      </Link>
    </>
  )
}

function SearchBar() {
  return (
    <form className={styles.form}>
      <input type="search" className={styles.input} name="keyword"></input>
      <button>
        <img src="/search-icon.png" className={styles.search}/>
      </button>
    </form>
  )
}

function Header() {
  return (
    <nav className={styles.nav}>
      <div className={styles.div}>
        <NavLogo />
        <NavMenu />
      </div>
      <div className={styles.div}>
        <SearchBar />
        <NavIcons />
      </div>
    </nav>
  )
}

export default Header