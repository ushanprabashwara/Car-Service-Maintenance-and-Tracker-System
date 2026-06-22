/* AutoTrack — Global API & Session Helpers */

const api = {
    async get(url) {
        const res = await fetch(url);
        if (res.ok) return res.json();
        throw new Error(await res.text());
    },
    async post(url, data) {
        const res = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const text = await res.text();
        if (res.ok) { try { return JSON.parse(text); } catch (e) { return text; } }
        throw new Error(text);
    },
    async put(url, data) {
        const res = await fetch(url, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const text = await res.text();
        if (res.ok) { try { return JSON.parse(text); } catch (e) { return text; } }
        throw new Error(text);
    },
    async delete(url) {
        const res = await fetch(url, { method: 'DELETE' });
        const text = await res.text();
        if (res.ok) return text;
        throw new Error(text);
    }
};

const session = {
    async check() {
        try { return await api.get('/api/users/me'); } catch (e) { return null; }
    },
    async logout() {
        await api.post('/api/users/logout', {});
        window.location.href = '/login';
    }
};

function notify(message, type = 'success') {
    const icons = {
        success: '<i class="fas fa-check-circle" style="color:#10b981;"></i>',
        error:   '<i class="fas fa-times-circle" style="color:#f43f5e;"></i>',
        warning: '<i class="fas fa-exclamation-triangle" style="color:#f59e0b;"></i>'
    };
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `${icons[type] || icons.success} <span style="margin-left:0.6rem;">${message}</span>`;
    document.body.appendChild(toast);
    setTimeout(() => {
        toast.style.animation = 'slideOut 0.3s forwards';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}
