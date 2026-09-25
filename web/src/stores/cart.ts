import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartApi } from '@/api/cart'
import type { CartView } from '@/types'

export const useCartStore = defineStore('cart', () => {
  const data = ref<CartView>({ totalCount: 0, totalPrice: 0, items: [] })
  const loaded = ref(false)
  const lastAdded = ref<string | null>(null)

  const totalCount = computed(() => data.value.totalCount)
  const hasItems = computed(() => data.value.items.length > 0)

  async function refresh() {
    data.value = await cartApi.list()
    loaded.value = true
    return data.value
  }

  async function add(id: number) {
    data.value = await cartApi.add(id)
    lastAdded.value = data.value.items.find((i) => i.id === id)?.name ?? null
  }

  async function updateCount(id: number, count: number) {
    data.value = await cartApi.updateCount(id, count)
  }

  async function remove(id: number) {
    data.value = await cartApi.remove(id)
  }

  async function clear() {
    data.value = await cartApi.clear()
  }

  async function checkout(): Promise<string> {
    const res = await cartApi.checkout()
    data.value = res.cart
    lastAdded.value = null
    return res.orderId
  }

  return { data, loaded, lastAdded, totalCount, hasItems, refresh, add, updateCount, remove, clear, checkout }
})
