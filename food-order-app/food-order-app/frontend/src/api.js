export const API = "http://localhost:8085/api";

async function handleResponse(r) {
  let data = {};
  try {
    data = await r.json();
  } catch {}
  if (!r.ok) {
    throw new Error(data.error || `İstek başarısız oldu (${r.status})`);
  }
  return data;
}

export async function post(path, body) {
  const token = localStorage.getItem("token");
  const headers = { "Content-Type": "application/json" };
  if (token) headers.Authorization = `Bearer ${token}`;

  const r = await fetch(`${API}${path}`, {
    method: "POST",
    headers,
    body: JSON.stringify(body),
  });
  return handleResponse(r);
}

export async function get(path) {
  const r = await fetch(`${API}${path}`);
  return handleResponse(r);
}

export async function getAuth(path) {
  const token = localStorage.getItem("token");
  if (!token) throw new Error("Oturum bulunamadı, lütfen tekrar giriş yapın.");

  const r = await fetch(`${API}${path}`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return handleResponse(r);
}

export async function put(path, body) {
  const token = localStorage.getItem("token");
  if (!token) throw new Error("Yetkilendirme hatası: Token bulunamadı.");

  const r = await fetch(`${API}${path}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  });
  return handleResponse(r);
}

export async function patch(path, body) {
  const token = localStorage.getItem("token");
  if (!token) throw new Error("Yetkilendirme hatası: Token bulunamadı.");

  const r = await fetch(`${API}${path}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  });
  return handleResponse(r);
}

export async function del(path) {
  const token = localStorage.getItem("token");
  if (!token) throw new Error("Yetkilendirme hatası: Token bulunamadı.");

  const r = await fetch(`${API}${path}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${token}` },
  });
  return handleResponse(r);
}
