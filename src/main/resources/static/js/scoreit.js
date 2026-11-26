document.addEventListener('click', (event) => {
    const opener = event.target.closest('[data-open]');
    if (opener) {
        const dialog = document.getElementById(opener.dataset.open);
        if (!dialog) return;
        dialog.querySelector('form')?.reset();
        fillDefaultTipOff(dialog);
        dialog.showModal();
        dialog.querySelector('input:not([type=hidden])')?.focus();
        return;
    }

    const closer = event.target.closest('[data-close]');
    if (closer) {
        closer.closest('dialog')?.close();
    }
});

// Switching player submits the form straight away
document.querySelectorAll('select[data-autosubmit]').forEach((select) => {
    select.addEventListener('change', () => select.form.submit());
});

// New matches default to tomorrow at 19:30
function fillDefaultTipOff(dialog) {
    const input = dialog.querySelector('[data-default-tomorrow]');
    if (!input) return;
    const date = new Date();
    date.setDate(date.getDate() + 1);
    const pad = (n) => String(n).padStart(2, '0');
    input.value = `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T19:30`;
}

// Messages fade out after a few seconds (errors stay a little longer)
document.querySelectorAll('.toast').forEach((toast) => {
    const delay = toast.classList.contains('error') ? 6000 : 3500;
    setTimeout(() => toast.classList.add('hide'), delay);
    setTimeout(() => toast.remove(), delay + 400);
});