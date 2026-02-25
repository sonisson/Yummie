import Footer from '/src/components/Footer/Footer.jsx'
import styles from './login.module.css'
import { GoogleLogin } from '@react-oauth/google'
import { Link } from 'react-router-dom'
import { googleLogout } from '@react-oauth/google'
import { useNavigate } from 'react-router-dom'
import { useState } from 'react'


function Form() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()
  const handleSubmit = (e) => {
    e.preventDefault()
    const data = { email, password }
    fetch('/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => {
      if (response.ok) {
        navigate('/menu?filter=top&page=1')
      }
    })
  }

  const handleSuccess = credential => {
    fetch("/api/auth/login/google", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ credential: credential })
    })
    .then(response=>{
      if(response.ok){
        navigate('/menu?filter=top&page=1')
      }
    })
    googleLogout()
  }

  return (
    <form className={styles.form} onSubmit={handleSubmit}>
      <h2>ĐĂNG NHẬP</h2>
      <p className={styles.error}>{error}</p>
      <input type="email" placeholder="Email *" require="true" value={email} onChange={(e) => setEmail(e.target.value)}></input>
      <input type="password" placeholder="Mật khẩu *" require="true" value={password} onChange={(e) => setPassword(e.target.value)}></input>
      <p className={styles.forget}>Quên mật khẩu?</p>
      <button type="submit">Đăng nhập</button>
      <p className={styles.or}>Hoặc</p>
      <div className={styles.google}>
        <GoogleLogin
          onSuccess={credentialResponse => handleSuccess(credentialResponse.credential)} />
      </div>
      <p className={styles.register}>Chưa có tài khoản? <Link className={styles.link} to="/register">Đăng ký</Link></p>
    </form>
  )
}

function Main() {
  return (
    <main className={styles.main}>
      <div className={styles.container}>
        <img src="/poster.jpg" />
        <Form />
      </div>
    </main>
  )
}

function Login() {
  return (
    <>
      <Main />
      <Footer />
    </>
  )
}

export default Login