<template>
  <div class="toast-container position-fixed end-0 p-3" style="top: 100px; z-index: 9999">
    <div
      ref="toastRef"
      class="toast text-white border-0 shadow"
      role="alert"
      aria-live="assertive"
      aria-atomic="true"
    >
      <div class="d-flex">
        <div class="toast-body fw-semibold d-flex align-items-center gap-2">
          <i :class="iconClass" class="fs-5"></i>
          <span v-html="message"></span>
        </div>
        <button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          data-bs-dismiss="toast"
          aria-label="Close"
        ></button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const toastRef = ref(null)
const message = ref('')
const iconClass = ref('bi bi-check-circle-fill')

const icons = {
  success: 'bi bi-check-circle-fill',
  error: 'bi bi-x-circle-fill',
  warning: 'bi bi-exclamation-triangle-fill',
  info: 'bi bi-info-circle-fill',
}

function show(msg, type = 'success') {
  message.value = msg
  iconClass.value = icons[type] || icons.success

  const el = toastRef.value
  el.classList.remove('bg-success', 'bg-danger', 'bg-warning', 'bg-info')
  el.classList.add(`bg-${type}`)

  const toast = new bootstrap.Toast(el, { delay: 4000, autohide: true })
  toast.show()
}

defineExpose({ show })
</script>

<style scoped>
.toast-body strong {
  color: #fff;
}
</style>
