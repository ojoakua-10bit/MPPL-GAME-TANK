# Dokumentasi Endpoint API

**DISCLAIMER**: Karena perubahan scope project, semua yang ada pada dokumentasi ini hanya yang berada pada scope admin. Meskipun pada kodingan server aslinya sudah terdapat scope player.

## Authentication

Selalu sediakan informasi berikut pada HTTP header ketika menggunakan API kami. Jika tidak, maka server akan memberikan respon `401 Unauthorized`.

Field | Type | Deskripsi
----|----|----
App-Token | 64-characters string | Token untuk mengidentifikasi aplikasi yang membuat request.
User-Token | 64-characters string | Token untuk mengidentifikasi user yang membuat request. 

Terdapat dua jenis `App-Token`: `ADMIN` dan `PLAYER`. `ADMIN` token digunakan untuk aplikasi khusus yang digunakan untuk mengelola data pada server pusat. `PLAYER` token digunakan pada game client. 

`User-Token` didapatkan dengan mengirim informasi username dan password via multipart form atau www-form-encoded. Hanya `App-Token` yang dibutuhkan dalam proses ini.

**PERHATIAN**: Me-request `User-Token` membuat `User-Token` yang lama menjadi *expired* untuk user tersebut. 

**`POST` /auth**

Gunakan endpint ini untuk me-request token untuk Player.

**`POST` /auth/admin**

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

**`POST` /admins**

Mendaftarkan admin baru. Hanya membutuhkan `App-Token` untuk ADMIN tanpa `User-Token`.

### Required Parameters

Field | Type | Deskripsi
----|----|----
username | string | Username admin yang akan didaftarkan.
password | string | Password dari admin yang akan didaftarkan.
admin_name | string | Nickname dari admin yang akan didaftarkan.


### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari proses pendaftaran. Bernilai `201 Created` jika pendaftaran berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Admin](#admin-object) object | Data dari Admin yang telah melakukan pendaftaran. Field ini berisi jika pendaftaran sukses.

## Edit Admin

**`PUT` /admins/{id}**

**`PATCH` /admins/{id}**

Mengubah data dari admin. Dibutuhkan `App-Token` dan `User-Token` Admin. Semua parameter di sini bersifat optional. Direkomendasikan untuk menggunakan `PUT` jika mengedit keseluruhan parameter dan `PATCH` jika mengedit sebagian saja.

### Required Parameters

Field | Type | Deskripsi
----|----|----
username | string | Nilai username baru yang telah diubah.
password | string | Nilai password baru yang telah diubah.
admin_name | string | Nilai nickname baru yang telah diubah.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari proses pengeditan. Bernilai `201 Created` jika pengeditan berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Admin](#admin-object) object | Data dari Admin yang telah diedit. Field ini berisi jika pengeditan sukses.

## Upload Avatar

**`POST` /uploads/images/admin/{id}**

Mengubah avatar atau profile image dari admin dengan id tersebut.

### Required Parameters

Field | Type | Deskripsi
----|----|----
image_data | Multipart file data | Data gambar yang akan diupload. Server akan menolak jika file gambar tidak valid.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari proses upload. Bernilai `201 Created` jika upload berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | string | Path relatif menuju file pada file server.

## Hapus Admin

**`DELETE` /admins/{id}**

Menghapus akun admin dengan id yang ditentukan pada path variable. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Tidak ada parameter dibutuhkan dalam proses ini.

### Response Structure

Sistem akan memberikan respon `204 No Content` jika proses delete berhasil.

---

## Tambah Stat

**`POST` /stats**

Menambahkan stat baru. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Field | Type | Deskripsi
----|----|----
stat_type | [StatType](#stattype-enum) enum | Jenis dari stat.
stat_name | string | Nama dari stat, bersifat unique setiap stat-nya.
stat_value | real | Nilai dari stat tersebut.


### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari penambahan stat. Bernilai `201 Created` jika penambahan berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Stat](#stat-object) object | Data dari Stat yang ditambahkan. Field ini berisi jika penambahan sukses.


## Mendapatkan Daftar Stat

**`GET` /stats**

Mendapatkan daftar stat yang ada di server. Me-return array dari [Stat](#stat-object).

### Required Parameters

Tidak ada parameter dibutuhkan dalam proses ini.

### Response Structure

Sistem akan me-return list dari stat berupa array of [Stat](#stat-object).

## Edit Stat

**`PUT` /admins/{id}**

**`PATCH` /admins/{id}**

Mengubah data dari stat. Dibutuhkan `App-Token` dan `User-Token` Admin. Semua parameter di sini bersifat optional.

### Required Parameters

Field | Type | Deskripsi
----|----|----
stat_type | [StatType](#stattype-enum) enum | Jenis dari stat.
stat_name | string | Nama dari stat, bersifat unique setiap stat-nya.
stat_value | real | Nilai dari stat tersebut.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari proses pengeditan. Bernilai `201 Created` jika pengeditan berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Stat](#stat-object) object | Data dari Stat yang sudah diedit. Field ini berisi jika pengeditan sukses.

## Delete Stat

**`DELETE` /stats/{id}**

Menghapus stat dengan id yang ditentukan pada path variable. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Tidak ada parameter dibutuhkan dalam proses ini.

### Response Structure

Sistem akan memberikan respon `204 No Content` jika proses delete berhasil.

---

## Tambah Item

**`POST` /items**

Menambahkan item baru. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Field | Type | Deskripsi
----|----|----
item_category | [ItemCategory](#itemcategory-enum) enum | Jenis dari item.
item_name | string | Nama untuk item game.
description | string | Penjelasan singkat tentang item game.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari penambahan stat. Bernilai `201 Created` jika penambahan berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Stat](#stat-object) object | Data dari Stat yang ditambahkan. Field ini berisi jika penambahan sukses.

## Mendapatkan Daftar Item

**`GET` /items**

Mendapatkan daftar item yang ada di server. Me-return array dari [Item](#item-object).

### Required Parameters

Parameter di sini semuanya bersifat opsional.

Field | Type | Deskripsi
----|----|----
start | integer | Nilai dari mana query dimulai.
n | integer | Nilai maksimum banyaknya data yang akan diquery.

### Response Structure

Sistem akan me-return list dari stat berupa array of [Stat](#stat-object).

## Edit Item

**`PUT` /admins/{id}**

**`PATCH` /admins/{id}**

Mengubah data dari stat. Dibutuhkan `App-Token` dan `User-Token` Admin. Semua parameter di sini bersifat optional.

### Required Parameters

Field | Type | Deskripsi
----|----|----
item_category | [ItemCategory](#itemcategory-enum) enum | Jenis dari item.
item_name | string | Nama untuk item game.
description | string | Penjelasan singkat tentang item game.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari pengeditan stat. Bernilai `201 Created` jika pengeditan berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Stat](#stat-object) object | Data dari Stat yang diedit. Field ini berisi jika pengeditan sukses.

## Upload Asset Model Item

**`POST` /uploads/assets/{id}**

Mengupload asset model untuk item dengan id tersebut.

### Required Parameters

Field | Type | Deskripsi
----|----|----
assets_data | Multipart file data | Data asset yang akan diupload.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari proses upload. Bernilai `201 Created` jika upload berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | string | Path relatif menuju file pada file server.

## Hapus Item

**`DELETE` /items/{id}**

Menghapus item dengan id yang ditentukan pada path variable. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Tidak ada parameter dibutuhkan dalam proses ini.

### Response Structure

Sistem akan memberikan respon `204 No Content` jika proses delete berhasil.

## Menambahkan Stat ke Item

**`POST` /items/{id}/stats**

Menambahkan stat ke dalam item dengan id tersebut.

### Required Parameters

Semua parameter di sini berupa query string dan harus diisi salah satu. Tidak boleh diisi keduanya.

Field | Type | Deskripsi
----|----|----
stat_id | number | Id dari [Stat](#stat-object)
stat_name | string | Nama dari stat tersebut.

### Response Structure

Sistem akan memberikan return berupa list dari stat yang ada pada item (array of Stat).

## Menghapus Stat dari Item

**`DELETE` /items/{id}/stats**

Menghapus suatu stat dari item dengan id tersebut.

### Required Parameters

Semua parameter di sini berupa query string dan harus diisi salah satu. Tidak boleh diisi keduanya.

Field | Type | Deskripsi
----|----|----
stat_id | number | Id dari [Stat](#stat-object)
stat_name | string | Nama dari stat tersebut.

### Response Structure

Sistem akan memberikan return berupa list dari stat yang ada pada item (array of Stat).

---

## Mencari Player

**`GET` /players**

Mencari player dengan username tertentu.

### Required Parameters

Field | Type | Deskripsi
----|----|----
q | string | Username dari player yang akan dicari. Bersifat opsional, jika diabaikan akan mereturn list seluruh player.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
matched_result | [Player](#player-object) object | Player yang memiliki username tepat sama dengan username yang dimaksud.
possible_results | array of [Player](#player-object) | Daftar player yang memiliki username yang mengandung unsur dari username yang dimaksud.

## Edit Player

**`PUT` /players/{id}**

**`PATCH` /players/{id}**

Mengedit data player yang memiliki id tersebut. Seluruh parameter bersifat opsional.

### Required Parameters

**CATATAN**: Parameter yang ada di list ini hanya yang berada pada scope admin.

Field | Type | Deskripsi
----|----|----
password | string | Password baru untuk player tersebut.
credit_balance | integer | Nilai credit baru untuk player tersebut.

### Response Structure

Field | Type | Deskripsi
----|----|----
timestamp | ISO8601 timestamp | Request timestamp.
response | string | Response code dari pengeditan player. Bernilai `201 Created` jika pengeditan berhasil.
message | string | Pesan berisi penjelasan dari response code.
data | [Player](#player-object) object | Data dari Player yang diedit. Field ini berisi jika pengeditan sukses.

## Set Ban Player

**`PUT` /players/{id}/ban**

**`PATCH` /players/{id}/ban**

Mengeset status ban dari player tersebut. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Field | Type | Deskripsi
----|----|----
value | boolean | Status ban untuk player tersebut.

### Response Structure

Sistem akan memberikan respon `204 No Content` jika proses set ban berhasil.

## Hapus Player

**`DELETE` /players/{id}**

Menghapus akun player dengan id yang ditentukan pada path variable. Dibutuhkan `App-Token` dan `User-Token` Admin.

### Required Parameters

Tidak ada parameter dibutuhkan dalam proses ini.

### Response Structure

Sistem akan memberikan respon `204 No Content` jika proses delete berhasil.

---

# Glosarium Object

## Player Object

Merepresentasikan informasi tentang player.

### Struktur Player

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

### Contoh Player Object
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

## Admin Object

Merepresentasikan informasi tentang admin.

### Struktur Admin

Field | Type | Deskripsi
----|----|----
admin_id | string | Unique id dari admin.
username | string | Unique username yang dapat mengidentifikasi admin. Username dapat diubah selama username tersebut tidak digunakan admin lain. 
player_name | string | Nama dari admin. Tidak harus bersifat unik dan tidak terikat limitasi username.
avatar | string | Path relatif dari file server menuju file avatar dari admin.

### Contoh Admin Object
```json
{
    "admin_id": "abc0bc19149aa",
    "username": "flux",
    "admin_name": "Flux Hydra",
    "avatar": "/static/images/admin/abc0bc19149aaZa-Flux.jpg"
}
```

## Item Object

Merepresentasikan informasi tentang game item.

### Struktur Item Object

Field | Type | Deskripsi
----|----|----
item_id | string | Unique id dari item game.
item_category | [ItemCategory](#itemcategory-enum) enum | Jenis dari item.
item_name | string | Nama untuk item game.
description | string | Penjelasan singkat tentang item game.
model_location | string | Path menuju file asset pada file server. Benilai `null` jika item tidak memiliki asset.

### ItemCategory enum

Value | Deskripsi
----|----
`TANK` | Item berupa tank.
`SKIN` | Item berupa skin dari tank.
`GAMEPLAY_DROPPED_ITEM` | Item merupakan bonus item yang didapatkan dari setelah match.
`INVENTORY_CAPACITY` | Item merupakan tambahan inventory capacity.

### Contoh Item Object
```json
{
    "item_id": "4ea8951cfa43ccbd3b9f",
    "item_category": "SKIN",
    "item_name": "Dummy Skin",
    "description": "This is dummy skin",
    "model_location": "/static/assets/4ea8951cfa43ccbd3b9fM3-Dummy-Skin-Asset.zip"
}
```

## Stat Object

Merepresentasikan informasi tentang stat pada sebuah game item.

### Struktur Stat Object

Field | Type | Deskripsi
----|----|----
stat_id | number | Unique id dari stat.
type | [StatType](#stattype-enum) enum | Jenis dari stat.
name | string | Nama dari stat, bersifat unique setiap stat-nya.
value | real | Nilai dari stat tersebut.

### StatType enum

Value | Deskripsi
----|----
`HITPOINT` | Stat berupa base hitpoint dari tank.
`ATTACK` | Stat berupa base attack dari tank.
`DEFENSE` | Stat berupa base defense dari tank
`SPEED` | Stat berupa base speed dari tank.
`RELOAD_SPEED` | Stat berupa base reload speed dari tank.
`HITPOINT_BOOST` | Stat berupa tambahan hitpoint untuk tank.
`ATTACK_BOOST` | Stat berupa tambahan attack untuk tank
`DEFENSE_BOOST` | Stat berupa tambahan defense untuk tank.
`SPEED_BOOST` | Stat berupa tambahan speed untuk tank.
`RELOAD_SPEED_BOOST` | Stat berupa tambahan reload speed untuk tank.
`INV_CAPACITY_BONUS` | Stat berupa tambahan kapasitas inventory untuk player.

### Contoh Stat Object
```json
{
    "stat_id": 7,
    "type": "HITPOINT_BOOST",
    "name": "Dummy HP Boost",
    "value": "160.0"
}
```
