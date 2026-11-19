import { reactive } from 'vue';

export const notifyState = reactive({
    toasts: [],
    modal: {
        isOpen: false,
        title: '',
        message: '',
        confirmText: 'Подтвердить',
        cancelText: 'Отмена',
        danger: false,
        resolve: null
    }
});

let nextId = 1;

const addToast = (message, type = 'info', duration = 3500) => {
    const id = nextId++;
    notifyState.toasts.push({ id, message, type });
    setTimeout(() => {
        removeToast(id);
    }, duration);
};

export const removeToast = (id) => {
    const index = notifyState.toasts.findIndex((t) => t.id === id);
    if (index !== -1) {
        notifyState.toasts.splice(index, 1);
    }
};

export const closeConfirm = (result) => {
    if (notifyState.modal.resolve) {
        notifyState.modal.resolve(result);
    }
    notifyState.modal.isOpen = false;
    notifyState.modal.resolve = null;
};

export const notify = {
    success(message) {
        addToast(message, 'success');
    },
    error(message) {
        addToast(message, 'error', 4500);
    },
    info(message) {
        addToast(message, 'info');
    },
    confirm({ title = 'Подтверждение', message, confirmText = 'Да', cancelText = 'Отмена', danger = false }) {
        return new Promise((resolve) => {
            notifyState.modal.title = title;
            notifyState.modal.message = message;
            notifyState.modal.confirmText = confirmText;
            notifyState.modal.cancelText = cancelText;
            notifyState.modal.danger = danger;
            notifyState.modal.resolve = resolve;
            notifyState.modal.isOpen = true;
        });
    }
};