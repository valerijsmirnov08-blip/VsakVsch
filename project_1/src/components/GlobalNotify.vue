<script setup>
import { notifyState, removeToast, closeConfirm } from '@/composables/useNotify.js';
</script>

<template>
    <Teleport to="body">
        <div class="toast_stack">
            <TransitionGroup name="toast">
                <div
                    v-for="toast in notifyState.toasts"
                    :key="toast.id"
                    :class="['toast_card', toast.type]"
                    @click="removeToast(toast.id)"
                >
                    <span class="toast_icon">
                        {{ toast.type === 'success' ? '✓' : toast.type === 'error' ? '!' : 'i' }}
                    </span>
                    <span class="toast_text">{{ toast.message }}</span>
                </div>
            </TransitionGroup>
        </div>

        <Transition name="fade">
            <div
                v-if="notifyState.modal.isOpen"
                class="modal_backdrop"
                @click.self="closeConfirm(false)"
            >
                <div class="modal_box">
                    <h3>{{ notifyState.modal.title }}</h3>
                    <p>{{ notifyState.modal.message }}</p>
                    <div class="modal_actions">
                        <button type="button" class="btn_cancel" @click="closeConfirm(false)">
                            {{ notifyState.modal.cancelText }}
                        </button>
                        <button
                            type="button"
                            :class="['btn_confirm', { danger: notifyState.modal.danger }]"
                            @click="closeConfirm(true)"
                        >
                            {{ notifyState.modal.confirmText }}
                        </button>
                    </div>
                </div>
            </div>
        </Transition>
    </Teleport>
</template>

<style scoped>
.toast_stack {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 9999;
    display: flex;
    flex-direction: column;
    gap: 10px;
    max-width: 360px;
    width: calc(100% - 40px);
    pointer-events: none;
}

.toast_card {
    pointer-events: auto;
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 14px 18px;
    border-radius: 18px;
    background: #ffffff;
    border: 1px solid #eee8f2;
    box-shadow: 0 12px 28px rgba(31, 41, 55, 0.12);
    font-family: system-ui, -apple-system, sans-serif;
    font-size: 14px;
    font-weight: 500;
    color: #1f2937;
    cursor: pointer;
}

.toast_icon {
    width: 28px;
    height: 28px;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 800;
    font-size: 14px;
    flex-shrink: 0;
}

.toast_card.success .toast_icon {
    background: #dcfce7;
    color: #15803d;
}

.toast_card.error .toast_icon {
    background: #fee2e2;
    color: #dc2626;
}

.toast_card.info .toast_icon {
    background: #ffedd5;
    color: #ea580c;
}

.toast-enter-active,
.toast-leave-active {
    transition: all 0.25s ease;
}

.toast-enter-from,
.toast-leave-to {
    opacity: 0;
    transform: translateY(-12px) scale(0.96);
}

.modal_backdrop {
    position: fixed;
    inset: 0;
    z-index: 9998;
    background: rgba(17, 24, 39, 0.45);
    backdrop-filter: blur(4px);
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 16px;
    font-family: system-ui, -apple-system, sans-serif;
}

.modal_box {
    background: #ffffff;
    border-radius: 26px;
    padding: 28px;
    max-width: 410px;
    width: 100%;
    box-shadow: 0 20px 45px rgba(0, 0, 0, 0.18);
    border: 1px solid #f0ebf4;
    text-align: center;
}

.modal_box h3 {
    margin: 0 0 10px;
    font-size: 20px;
    color: #111827;
}

.modal_box p {
    margin: 0 0 24px;
    font-size: 15px;
    line-height: 1.5;
    color: #4b5563;
}

.modal_actions {
    display: flex;
    gap: 12px;
}

.modal_actions button {
    flex: 1;
    padding: 13px 16px;
    border-radius: 16px;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
    border: none;
    transition: transform 0.15s, opacity 0.15s;
}

.modal_actions button:active {
    transform: scale(0.98);
}

.btn_cancel {
    background: #f3f0f6;
    color: #4b5563;
}

.btn_cancel:hover {
    background: #e7e1ed;
}

.btn_confirm {
    background: linear-gradient(to right, #ff7e5f, #feb47b);
    color: #ffffff;
    box-shadow: 0 6px 16px rgba(255, 126, 95, 0.25);
}

.btn_confirm.danger {
    background: linear-gradient(to right, #ef4444, #f87171);
    box-shadow: 0 6px 16px rgba(239, 68, 68, 0.25);
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

@media (max-width: 480px) {
    .toast_stack {
        top: 12px;
        right: 12px;
        left: 12px;
        width: auto;
        max-width: none;
    }

    .modal_box {
        padding: 22px 18px;
    }

    .modal_actions {
        flex-direction: column-reverse;
    }
}
</style>