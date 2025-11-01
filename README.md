#  FoodOrder — Online Yemek Sipariş Sistemi

**Bu proje; müşteri, restoran sahibi ve admin rolleriyle çalışan, kullanıcıların restoranlardan yemek sipariş verebildiği full-stack yemek sipariş uygulamasıdır.**

---

##  Proje Özeti

# Özellik | Açıklama 

 **Customer** : Restoranları görüntüler, menü inceler, sepete ürün ekler, sipariş verir  
 **Restaurant Owner** : Restoranını yönetir, menü/ürün ekler-düzenler, gelen siparişleri görür  
 **Admin** : Sistemdeki tüm siparişleri görüntüler  
 **Kimlik Doğrulama** : JWT tabanlı oturum sistemi  
 **Şifre Yenileme** : "Şifremi Unuttum" üzerinden yeni şifre belirleme  

---

##  Kullanılan Teknolojiler

### Frontend
- React (Vite)
- React Router DOM
- Context API (Sepet yönetimi)
- CSS

### Backend
- Java 17
- Spring Boot (Web, Security, Data JPA)
- JWT Authentication
- H2 Database
- Maven

---
##  Kurulum ve Çalıştırma

###  Backend Kurulum
 # cd backend
# mvn clean install
# mvn spring-boot:run

### Frontend Kurulum
# cd frontend
# npm install
# npm run dev


