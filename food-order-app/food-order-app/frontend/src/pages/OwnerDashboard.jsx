import { useEffect, useState } from "react";
import { getAuth, get, post, put, del } from "../api";
import { useNavigate } from "react-router-dom";
import "./OwnerDashboard.css";
import Navbar from "../components/Navbar";

export default function OwnerDashboard() {
  const [restaurants, setRestaurants] = useState([]);
  const [selected, setSelected] = useState(null);
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({
    name: "",
    logoUrl: "",
    address: "",
    description: "",
  });
  const [menuForm, setMenuForm] = useState({
    name: "",
    price: "",
    description: "",
    stock: 0,
    imageUrl: "",
  });
  const nav = useNavigate();

  useEffect(() => {
    loadRestaurants();
  }, []);

  async function loadRestaurants() {
    try {
      const list = await getAuth("/owner/restaurants/mine");
      setRestaurants(list);
    } catch (err) {
      console.error("Restoranlar yüklenemedi:", err);
    }
  }

  async function selectRestaurant(r) {
    setSelected(r);
    const list = await get(`/restaurants/${r.id}/products`);
    setProducts(list.content || []);
  }

  async function createRestaurant() {
    try {
      await post("/owner/restaurants", form);
      alert("Restoran oluşturuldu!");
      setForm({ name: "", logoUrl: "", address: "", description: "" });
      await loadRestaurants();

      const updatedList = await get("/restaurants");
      localStorage.setItem("restaurants_cache", JSON.stringify(updatedList));
    } catch (err) {
      alert("Hata: " + err.message);
    }
  }

  async function updateRestaurant() {
    try {
      await put(`/owner/restaurants/${selected.id}`, form);
      alert("Restoran güncellendi!");
      loadRestaurants();
    } catch (err) {
      alert("Güncelleme hatası: " + err.message);
    }
  }

  async function deleteRestaurant(id) {
    if (!window.confirm("Bu restoranı silmek istediğine emin misin?")) return;
    await del(`/owner/restaurants/${id}`);
    alert("Restoran silindi!");
    setSelected(null);
    loadRestaurants();
  }

  async function addProduct() {
    try {
      await post(`/owner/restaurants/${selected.id}/products`, menuForm);
      alert("Ürün eklendi!");
      setMenuForm({
        name: "",
        price: "",
        description: "",
        stock: 0,
        imageUrl: "",
      });
      selectRestaurant(selected);
    } catch (err) {
      alert("Ürün eklenemedi: " + err.message);
    }
  }

  function logout() {
    localStorage.clear();
    nav("/login");
  }

  return (
    <div className="owner-page">
      <Navbar />

      <main className="owner-main">
        <div className="restaurants-panel">
          <h3>Restoranlarım</h3>
          <ul>
            {restaurants.map((r) => (
              <li
                key={r.id}
                onClick={() => selectRestaurant(r)}
                className={selected?.id === r.id ? "active" : ""}
              >
                {r.name}
              </li>
            ))}
          </ul>

          <div className="form-section">
            <h4>Yeni Restoran Ekle</h4>
            <input
              placeholder="İsim"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
            <input
              placeholder="Logo URL"
              value={form.logoUrl}
              onChange={(e) => setForm({ ...form, logoUrl: e.target.value })}
            />
            <input
              placeholder="Adres"
              value={form.address}
              onChange={(e) => setForm({ ...form, address: e.target.value })}
            />
            <textarea
              placeholder="Açıklama"
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
            />
            <button onClick={createRestaurant}>Oluştur</button>
          </div>
        </div>

        <div className="restaurant-detail">
          {selected ? (
            <>
              <h3>{selected.name}</h3>
              <div className="form-section">
                <input
                  placeholder="İsim"
                  value={form.name || selected.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                />
                <input
                  placeholder="Logo URL"
                  value={form.logoUrl || selected.logoUrl}
                  onChange={(e) =>
                    setForm({ ...form, logoUrl: e.target.value })
                  }
                />
                <input
                  placeholder="Adres"
                  value={form.address || selected.address}
                  onChange={(e) =>
                    setForm({ ...form, address: e.target.value })
                  }
                />
                <textarea
                  placeholder="Açıklama"
                  value={form.description || selected.description}
                  onChange={(e) =>
                    setForm({ ...form, description: e.target.value })
                  }
                />
                <button onClick={updateRestaurant}>Güncelle</button>
                <button
                  className="danger"
                  onClick={() => deleteRestaurant(selected.id)}
                >
                  Sil
                </button>
              </div>

              <div className="menu-section">
                <h4>Menü Öğeleri</h4>
                <div className="menu-form">
                  <input
                    placeholder="Ürün adı"
                    value={menuForm.name}
                    onChange={(e) =>
                      setMenuForm({ ...menuForm, name: e.target.value })
                    }
                  />
                  <input
                    type="number"
                    placeholder="Fiyat"
                    value={menuForm.price}
                    onChange={(e) =>
                      setMenuForm({ ...menuForm, price: e.target.value })
                    }
                  />
                  <textarea
                    placeholder="Açıklama"
                    value={menuForm.description}
                    onChange={(e) =>
                      setMenuForm({ ...menuForm, description: e.target.value })
                    }
                  />
                  <input
                    type="number"
                    placeholder="Stok"
                    value={menuForm.stock}
                    onChange={(e) =>
                      setMenuForm({ ...menuForm, stock: e.target.value })
                    }
                  />
                  <input
                    placeholder="Görsel URL"
                    value={menuForm.imageUrl}
                    onChange={(e) =>
                      setMenuForm({ ...menuForm, imageUrl: e.target.value })
                    }
                  />
                  <button onClick={addProduct}>Ürün Ekle</button>
                </div>

                <div className="menu-list">
                  {products.length === 0 ? (
                    <p>Henüz ürün yok.</p>
                  ) : (
                    products.map((p) => (
                      <div key={p.id} className="menu-item">
                        <img
                          src={p.imageUrl || "https://via.placeholder.com/80"}
                          alt={p.name}
                        />
                        <div>
                          <h5>{p.name}</h5>
                          <p>{p.price} ₺</p>
                          <p>{p.description}</p>
                        </div>
                      </div>
                    ))
                  )}
                </div>
              </div>
            </>
          ) : (
            <p>Bir restoran seç veya yeni ekle.</p>
          )}
        </div>
      </main>
    </div>
  );
}
