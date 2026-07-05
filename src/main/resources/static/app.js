// URL base de la API. Si el front se sirve desde el mismo Spring Boot queda vacio.
// Si lo publicas en GitHub Pages, cambia por: "http://localhost:8080"
const API = "";

// Usuario demo (creado por el DataSeeder)
const USUARIO_ID = 1;

let carrito = []; // { productoId, nombre, precio, cantidad }

// ---------- navegacion entre secciones ----------
document.querySelectorAll(".nav-links a").forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault();
        document.querySelectorAll(".nav-links a").forEach(a => a.classList.remove("active"));
        link.classList.add("active");
        const id = link.dataset.seccion;
        document.querySelectorAll(".seccion").forEach(s => s.classList.remove("activa"));
        document.getElementById(id).classList.add("activa");
        if (id === "productos") cargarProductos();
        if (id === "categorias") cargarCategorias();
        if (id === "carrito") cargarSelectProductos();
        if (id === "pedidos") cargarPedidos();
    });
});

// ---------- PRODUCTOS ----------
async function cargarProductos() {
    const res = await fetch(`${API}/api/productos`);
    const productos = await res.json();
    const tbody = document.getElementById("tabla-productos");
    tbody.innerHTML = "";
    productos.forEach(p => {
        tbody.innerHTML += `<tr>
            <td>${p.id}</td>
            <td>${p.nombre}</td>
            <td>$${p.precio.toFixed(2)}</td>
            <td>${p.categoriaNombre ?? "-"}</td>
            <td>${p.stock}</td>
            <td><button class="btn-mini" onclick="eliminarProducto(${p.id})">🗑</button></td>
        </tr>`;
    });
}

async function eliminarProducto(id) {
    await fetch(`${API}/api/productos/${id}`, { method: "DELETE" });
    cargarProductos();
}

document.getElementById("btn-nuevo-producto").addEventListener("click", async () => {
    document.getElementById("form-producto").classList.toggle("oculto");
    await llenarSelectCategorias("p-categoria");
});

document.getElementById("btn-guardar-producto").addEventListener("click", async () => {
    const body = {
        nombre: document.getElementById("p-nombre").value,
        descripcion: document.getElementById("p-descripcion").value,
        precio: parseFloat(document.getElementById("p-precio").value),
        stock: parseInt(document.getElementById("p-stock").value),
        imagenUrl: document.getElementById("p-imagen").value,
        categoriaId: parseInt(document.getElementById("p-categoria").value)
    };
    const res = await fetch(`${API}/api/productos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    const msg = document.getElementById("msg-producto");
    if (res.ok) {
        msg.textContent = "Producto creado.";
        msg.className = "msg ok";
        document.getElementById("form-producto").classList.add("oculto");
        cargarProductos();
    } else {
        const err = await res.json();
        msg.textContent = "Error: " + (err.mensaje || JSON.stringify(err.detalles || err));
        msg.className = "msg error";
    }
});

// ---------- CATEGORIAS ----------
async function cargarCategorias() {
    const res = await fetch(`${API}/api/categorias`);
    const cats = await res.json();
    const ul = document.getElementById("lista-categorias");
    ul.innerHTML = "";
    cats.forEach(c => {
        ul.innerHTML += `<li><span>${c.nombre}</span>
            <button class="btn-mini" onclick="eliminarCategoria(${c.id})">🗑</button></li>`;
    });
}

async function eliminarCategoria(id) {
    await fetch(`${API}/api/categorias/${id}`, { method: "DELETE" });
    cargarCategorias();
}

document.getElementById("btn-guardar-categoria").addEventListener("click", async () => {
    const nombre = document.getElementById("c-nombre").value;
    await fetch(`${API}/api/categorias`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre })
    });
    document.getElementById("c-nombre").value = "";
    cargarCategorias();
});

async function llenarSelectCategorias(selectId) {
    const res = await fetch(`${API}/api/categorias`);
    const cats = await res.json();
    const sel = document.getElementById(selectId);
    sel.innerHTML = "";
    cats.forEach(c => sel.innerHTML += `<option value="${c.id}">${c.nombre}</option>`);
}

// ---------- CARRITO ----------
let productosCache = [];
async function cargarSelectProductos() {
    const res = await fetch(`${API}/api/productos`);
    productosCache = await res.json();
    const sel = document.getElementById("car-producto");
    sel.innerHTML = "";
    productosCache.forEach(p =>
        sel.innerHTML += `<option value="${p.id}">${p.nombre} ($${p.precio.toFixed(2)})</option>`);
    renderCarrito();
}

document.getElementById("btn-agregar-carrito").addEventListener("click", () => {
    const id = parseInt(document.getElementById("car-producto").value);
    const cantidad = parseInt(document.getElementById("car-cantidad").value);
    const prod = productosCache.find(p => p.id === id);
    if (!prod || cantidad < 1) return;
    const existente = carrito.find(i => i.productoId === id);
    if (existente) existente.cantidad += cantidad;
    else carrito.push({ productoId: id, nombre: prod.nombre, precio: prod.precio, cantidad });
    renderCarrito();
});

function renderCarrito() {
    const tbody = document.getElementById("tabla-carrito");
    tbody.innerHTML = "";
    let total = 0;
    carrito.forEach(i => {
        const sub = i.precio * i.cantidad;
        total += sub;
        tbody.innerHTML += `<tr>
            <td>${i.nombre}</td><td>${i.cantidad}</td>
            <td>$${i.precio.toFixed(2)}</td><td>$${sub.toFixed(2)}</td></tr>`;
    });
    document.getElementById("carrito-total").textContent = total.toFixed(2);
}

document.getElementById("btn-realizar-pedido").addEventListener("click", async () => {
    const msg = document.getElementById("msg-carrito");
    if (carrito.length === 0) {
        msg.textContent = "El carrito está vacío.";
        msg.className = "msg error";
        return;
    }
    const body = {
        usuarioId: USUARIO_ID,
        itemsPedido: carrito.map(i => ({ productoId: i.productoId, cantidad: i.cantidad }))
    };
    const res = await fetch(`${API}/api/pedidos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    if (res.ok) {
        const pedido = await res.json();
        msg.textContent = `Pedido #${pedido.id} creado. Total: $${pedido.total.toFixed(2)}`;
        msg.className = "msg ok";
        carrito = [];
        renderCarrito();
    } else {
        const err = await res.json();
        msg.textContent = "Error: " + (err.mensaje || JSON.stringify(err.detalles || err));
        msg.className = "msg error";
    }
});

// ---------- PEDIDOS ----------
async function cargarPedidos() {
    const res = await fetch(`${API}/api/usuarios/${USUARIO_ID}/pedidos`);
    const pedidos = await res.json();
    const cont = document.getElementById("lista-pedidos");
    cont.innerHTML = pedidos.length ? "" : "<p>No hay pedidos todavía.</p>";
    pedidos.forEach(p => {
        const items = p.items.map(i =>
            `<li>${i.nombreProducto} x${i.cantidad} — $${i.subtotal.toFixed(2)}</li>`).join("");
        cont.innerHTML += `<div class="pedido">
            <div class="cabecera">
                <strong>Pedido #${p.id}</strong>
                <span class="badge ${p.estado}">${p.estado}</span>
            </div>
            <p>${new Date(p.fecha).toLocaleString()} · Total: $${p.total.toFixed(2)}</p>
            <ul>${items}</ul>
        </div>`;
    });
}

// carga inicial
cargarProductos();
