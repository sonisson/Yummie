import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Register from './pages/auth/Register/Register.jsx'
import Login from './pages/auth/Login/Login.jsx'
import Menu from './pages/public/Menu/Menu.jsx'
import Product from './pages/public/Product/Product.jsx'
import Demo from './pages/demo/demo.jsx'
import Demo2 from './pages/demo2/demo2.jsx'
import Cart from './pages/user/Cart/Cart.jsx'
import Checkout from './pages/user/Checkout/Checkout.jsx'
import Profile from './pages/user/Profile/Profile.jsx'
import ShippingInformation from './pages/user/ShippingInformation/ShippingInformation.jsx'
import Orders from './pages/user/Orders/Orders.jsx'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/demo" element={<Demo />} />
        <Route path="/demo2" element={<Demo2 />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/drink" element={<Menu />} />
        <Route path="/food" element={<Menu />} />
        <Route path="/combo" element={<Menu />} />
        <Route path="/products/:id" element={<Product />} />
        <Route path="/cart" element={<Cart/>} />
        <Route path="/checkout" element={<Checkout/>} />
        <Route path="/profile" element={<Profile/>} />
        <Route path="/shipping-information" element={<ShippingInformation/>}/>
        <Route path="/orders" element={<Orders/>}/>
      </Routes>
    </Router>
  );
}

export default App