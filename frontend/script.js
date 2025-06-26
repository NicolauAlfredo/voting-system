/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

const API_BASE = 'http://localhost:8000';
document.getElementById('candidate-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = parseInt(document.getElementById('candidate-id').value);
    const name = document.getElementById('candidate-name').value;

    await fetch(`${API_BASE}/candidates`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({id, name})
    });

    alert("Candidate added!");
    document.getElementById('candidate-form').reset();
    loadCandidates();
});

document.getElementById('vote-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const voterId = document.getElementById('voter-id').value;
    const candidateId = parseInt(document.getElementById('candidate-select').value);

    const res = await fetch(`${API_BASE}/vote`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({voterId, candidateId})
    });

    const result = await res.json();
    const msg = document.getElementById('vote-message');
    msg.textContent = result.message || result.error;

    if (res.ok) {
        msg.style.color = 'green';
        document.getElementById('vote-form').reset();
        loadResults();
    } else {
        msg.style.color = 'red';
    }
});

async function loadCandidates() {
    const res = await fetch(`${API_BASE}/candidates`);
    const candidates = await res.json();
    const select = document.getElementById('candidate-select');
    select.innerHTML = '';

    candidates.forEach(c => {
        const option = document.createElement('option');
        option.value = c.id;
        option.textContent = `${c.name} (ID: ${c.id})`;
        select.appendChild(option);
    });
}

async function loadResults() {
    const res = await fetch(`${API_BASE}/results`);
    const results = await res.json();
    const list = document.getElementById('results-list');
    list.innerHTML = '';

    for (const [name, count] of Object.entries(results)) {
        const item = document.createElement('li');
        item.textContent = `${name}: ${count} vote(s)`;
        list.appendChild(item);
    }
}

// Inicializar
loadCandidates();
loadResults();
