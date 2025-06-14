import { reactive } from 'vue'

const CartStore = reactive({
  cartCount: 0,

  updateCount() {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    const maKH = userInfo?.maKH || null
    const storageKey = maKH ? `cart_${maKH}` : 'cart_temp'
    const cart = JSON.parse(localStorage.getItem(storageKey)) || []
    this.cartCount = cart.reduce((sum, item) => sum + (item.soLuong || 1), 0)
  },
})

export default CartStore
