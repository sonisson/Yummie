// function Payment() {
//   const handleCheckout = async () => {
//     // ví dụ: 100,000 VND
//     const amount = 100000;
//     const resp = await fetch(`http://localhost:8080/create-payment?amount=${amount}`);
//     const payUrl = await resp.text();
//     window.location.href = payUrl; // chuyển sang trang VNPay Sandbox
//   };

//   return (
//     <div style={{ padding: 24 }}>
//       <h2>Thanh toán VNPay</h2>
//       <p>Số tiền: 100,000 VND</p>
//       <button onClick={handleCheckout}>Thanh toán</button>
//     </div>
//   );
// }

// export default Payment
