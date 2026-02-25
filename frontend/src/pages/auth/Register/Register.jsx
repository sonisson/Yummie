import Footer from '/src/components/Footer/Footer.jsx'
import styles from './register.module.css'
import { Link, useNavigate } from 'react-router-dom'
import { useState, useEffect, useRef } from 'react'


function Verify({ email, password, sendAgain, onClose }) {
  const [code, setCode] = useState('')
  const [seconds, setSeconds] = useState(5)
  const timerRef = useRef(null)
  const [error, setError] = useState("")
  const navigate = useNavigate()
  const inputRef = useRef(null)

  inputRef.current?.focus()
  const countdown = () => {
    clearInterval(timerRef.current)
    setError("")
    setSeconds(120)
    timerRef.current = setInterval(() => {
      setSeconds(prev => {
        if (prev < 1) {
          clearInterval(timerRef.current)
          timerRef.current = null
          return 0
        }
        return prev - 1
      })
    }, 1000)
  }
  useEffect(() => {
    countdown()
    return () => clearInterval(timerRef.current)
  }, [])

  const handleSendAgain = (e) => {
    setCode("")
    sendAgain(e)
    countdown()
  }

  const handleChange = (value) => {
    if (/^\d*$/.test(value)) {
      setCode(value)
      setError("")
      if (value.length === 6) {
        const data = { email, password, code: value }
        fetch('/api/auth/register/verify', {
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
    }
  }
  return (
    <div className={styles.verifyContainer}>
      <div className={styles.verifyForm}>
        <p>Mã xác thực được gửi đến {email}</p>
        <input ref={inputRef} type="text" value={code} maxLength={6} onChange={(e) => { handleChange(e.target.value) }} />
        <p>{error}</p>
        {seconds >= 1 ? <><p>Mã xác thực hết hạn trong {seconds}s</p>
          <p>Không nhận được mã xác thực? <span onClick={(e) => handleSendAgain(e)}>Gửi lại</span></p></>
          : <p>Mã xác thực đã hết hạn. <span onClick={(e) => handleSendAgain(e)}>Gửi lại</span></p>}
        <button onClick={onClose}>Đóng</button>
      </div>
    </div>
  )
}

function Form() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [cfPassword, setCfPassword] = useState('')
  const [error, setError] = useState('')
  const [showVerify, setShowVerify] = useState(false)

  const handleSubmit = (e) => {
    e.preventDefault()
    if (password != cfPassword) {
      setError("Mật khấu xác nhận không khớp. Vui lòng nhập lại.")
    }
    else {
      const data = { email, password }
      fetch('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      })
        .then(response => {
          if (response.ok) {
            setShowVerify(true)
            setError("")
          }
        })
    }
  }

  return (
    <>
      <form className={styles.form} onSubmit={handleSubmit}>
        <h2>ĐĂNG KÝ</h2>
        <p>{error}</p>
        <input placeholder="Họ và tên *"></input>
        <input type="tel" placeholder="Số điện thoại *"></input>
        <input placeholder="Địa chỉ *"></input>
        <input type="email" placeholder="Email *" require="true" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="password" placeholder="Mật khẩu *" require="true" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input type="password" placeholder="Nhập lại mật khẩu *" require="true" value={cfPassword} onChange={(e) => setCfPassword(e.target.value)} />
        <button type="submit">Đăng ký</button>
        <p>Đã có tài khoản? <Link className={styles.link} to="/login">Đăng nhập</Link></p>
      </form>
      {showVerify && <Verify email={email} password={password} sendAgain={handleSubmit} onClose={() => setShowVerify(false)} />}
    </>
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

function Register() {
  return (
    <>
      <Main />
      <Footer />
    </>
  )
}

export default Register