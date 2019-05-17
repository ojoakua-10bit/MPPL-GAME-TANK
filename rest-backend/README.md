# rest-backend <!-- omit in toc -->

Backend REST API untuk tank game.

# Dokumentasi

## Daftar Isi

- [Dokumentasi](#dokumentasi)
  - [Daftar Isi](#daftar-isi)
  - [Authentication](#authentication)
    - [Required Parameters](#required-parameters)
    - [Response Structure](#response-structure)
  - [Tambah Admin](#tambah-admin)
    - [Required Parameters](#required-parameters-1)
    - [Response Structure](#response-structure-1)
  - [Edit Admin](#edit-admin)
    - [Required Parameters](#required-parameters-2)
    - [Response Structure](#response-structure-2)
  - [Hapus Admin](#hapus-admin)
    - [Required Parameters](#required-parameters-3)
    - [Response Structure](#response-structure-3)
  - [Tambah Stat](#tambah-stat)
  - [Mendapatkan Stat](#mendapatkan-stat)
  - [Edit Stat](#edit-stat)
  - [Delete Stat](#delete-stat)
  - [Tambah Item](#tambah-item)
  - [Mendapatkan Item](#mendapatkan-item)
  - [Edit Item](#edit-item)
  - [Hapus Item](#hapus-item)
  - [Menambahkan Stat ke Item](#menambahkan-stat-ke-item)
  - [Menghapus Stat dari Item](#menghapus-stat-dari-item)
  - [Tambah Player](#tambah-player)
  - [Mendapatkan Player](#mendapatkan-player)
  - [Edit Player](#edit-player)
  - [Hapus Player](#hapus-player)
  - [Glosarium Object](#glosarium-object)
    - [Player Object](#player-object)
      - [Struktur Player](#struktur-player)
      - [Contoh Player Object](#contoh-player-object)
    - [Admin Object](#admin-object)
      - [Struktur Admin](#struktur-admin)
      - [Contoh Admin Object](#contoh-admin-object)
    - [Item Object](#item-object)
      - [Struktur Item Object](#struktur-item-object)
      - [Contoh Item Object](#contoh-item-object)
    - [Stat Object](#stat-object)
      - [Struktur Stat Object](#struktur-stat-object)
      - [Contoh Stat Object](#contoh-stat-object)
    - [ShopItem Object](#shopitem-object)
      - [Struktur ShopItem Object](#struktur-shopitem-object)
      - [Contoh ShopItem Object](#contoh-shopitem-object)
    - [Match Object](#match-object)
      - [Struktur Match Object](#struktur-match-object)
      - [Contoh Match Object](#contoh-match-object)
    - [PlayerInventory Object](#playerinventory-object)
      - [Struktur PlayerInventory Object](#struktur-playerinventory-object)
      - [Contoh PlayerInventory Object](#contoh-playerinventory-object)

## Authentication

Selelu sediakan informasi berikut pada HTTP header ketika menggunakan API kami. Jika tidak, maka server akan memberikan respon `401 Unauthorized`.

Field | Type | Deskripsi
----|----|----
App-Token | 64-characters string | Token untuk mengidentifikasi aplikasi yang membuat request.
User-Token | 64-characters string | Token untuk mengidentifikasi user yang membuat request. 

Terdapat dua jenis `App-Token`: `ADMIN` dan `PLAYER`. `ADMIN` token digunakan untuk aplikasi khusus yang digunakan untuk mengelola data pada server pusat. `PLAYER` token digunakan pada game client. 

 `User-Token` didapatkan dengan mengirim informasi username dan password via multipart form atau www-form-encoded. Hanya `App-Token` yang dibutuhkan dalam proses ini.

**PERHATIAN**: Me-request `User-Token` membuat `User-Token` yang lama menjadi *expired* untuk user tersebut. 

### `POST` /auth <!-- omit in toc -->

Gunakan endpint ini untuk me-request token untuk Player.

### `POST` /auth/admin <!-- omit in toc -->

Gunakan endpoint ini untuk me-request token untuk Admin.

### Required Parameters

Field | Type | Deskripsi
----|----|----
username | string | Username dari user.
password | string | Password dari user.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
token | 64-characters string | Requested user token.
data | [Player](#player-object) atau [Admin](#admin-object) object | Data dari user yang melakukan request token.

---

## Tambah Admin

### `POST` /admins <!-- omit in toc -->

Mendaftarkan admin baru dari portal admin. Hanya membutuhkan `App-Token` untuk ADMIN tanpa `User-Token`.

### Required Parameters

### Response Structure

## Edit Admin

### `PUT` /admins/{id} <!-- omit in toc -->

### `PATCH` /admins/{id} <!-- omit in toc -->

### Required Parameters

### Response Structure

## Hapus Admin

### `DELETE` /admins/{id} <!-- omit in toc -->

### Required Parameters

### Response Structure

---

## Tambah Stat

## Mendapatkan Stat

## Edit Stat

## Delete Stat

---

## Tambah Item

## Mendapatkan Item

## Edit Item

## Hapus Item

## Menambahkan Stat ke Item

## Menghapus Stat dari Item

---

## Tambah Player

## Mendapatkan Player

## Edit Player

## Hapus Player

---

## Glosarium Object

### Player Object

Merepresentasikan informasi tentang player.

#### Struktur Player

Field | Type | Deskripsi
----|----|----
player_id | string | Unique id dari player.
username | string | Unique username yang dapat mengidentifikasi player. Username dapat diubah selama username tersebut tidak digunakan player lain. 
player_name | string | Nama dari player. Tidak harus bersifat unik dan tidak terikat limitasi username.
xp | integer | XP points yang didapatkan player.
rank | integer | Rank dari player. Berdasarkan XP points player.
diamond_count | integer | Jumlah diamond player.
gold_count | integer | Jumlah gold player.
credit_balance | integer | Jumlah credit player.
inventory | integer | Kapasitas inventory player saat ini.
avatar | string | Path relatif dari file server menuju file avatar dari player.
online_status | boolean | Status apakah user ini sedang online.
ban_status | boolean | Status apakah user ini sedang di-ban.

#### Contoh Player Object
```json
{
    "player_id": "0d89db5aae3bf7797def",
    "username": "anko",
    "player_name": "Ichinomiya Anko",
    "xp": 23750,
    "rank": 4,
    "diamond_count": 87,
    "gold_count": 11300,
    "credit_balance": 169000,
    "inventory": 15,
    "avatar": "/static/images/0d89db5aae3bf7797def-anko.png",
    "online_status": false,
    "ban_status": false
}
```

### Admin Object

Merepresentasikan informasi tentang admin.

#### Struktur Admin

Field | Type | Deskripsi
----|----|----
admin_id | string | Unique id dari admin.
username | string | Unique username yang dapat mengidentifikasi admin. Username dapat diubah selama username tersebut tidak digunakan admin lain. 
player_name | string | Nama dari admin. Tidak harus bersifat unik dan tidak terikat limitasi username.
avatar | string | Path relatif dari file server menuju file avatar dari admin.

#### Contoh Admin Object
```json
{
    "admin_id": "abc0bc19149aa",
    "username": "flux",
    "admin_name": "Flux Hydra",
    "avatar": "/static/images/admin/abc0bc19149aaZa-Flux.jpg"
}
```

### Item Object

Merepresentasikan informasi tentang game item.

#### Struktur Item Object



#### Contoh Item Object

### Stat Object

#### Struktur Stat Object

#### Contoh Stat Object

### ShopItem Object

#### Struktur ShopItem Object

#### Contoh ShopItem Object

### Match Object

#### Struktur Match Object

#### Contoh Match Object

### PlayerInventory Object

#### Struktur PlayerInventory Object

#### Contoh PlayerInventory Object

