# webshop-rest-api

Webshop REST API can have two type of users, CUSTOMER and ADMIN role, these roles have different permissions.
Customer role can order products, edit his personal info, reset password, etc.
Admin role can create, update, delete items, view top selled items, user info, orders, etc.

After registration every user recieves verification link to verify his email address, also user is innnactive until he confirm email.

Item images are stored at Amazon S3 service.

More other enhacements coming.

### security(all paths are public)

* POST - /api/v1/auth - this is the entry point for authentification user send username and passsword, api return jwt
* POST - /api/v1/registration - user registration
* POST - /api/v1/confirm-email - path for confirming email address


### users (authenticated)

* GET - /api/v1/users  - get all users
* GET - /api/v1/users/{userId} - get user by id (User with CUSTOMER role can get only his own data)
* GET - /api/v1/users/?username - get user by id
* PUT - /api/v1/users/{userId} - update user (Only for CUSTOMER roles)
* DELETE - /api/v1/users/{userId} - delete user (Only for ADMIN roles)

### shopping-cart (all path are allowed only for CUSTOMER roles)

* GET - /api/v1/users/{userId}/shopping-cart - get shopping cart
* POST - /api/v1/users/{userId}/init - init shopping cart 
* GET - /api/v1/users/{userId}/shopping-cart/cart-items - get items in shopping cart
* POST - /api/v1/users/{userId}/shopping-cart/cart-items - add items to shopping cart
* DELETE - /api/v1/users/{userId}/shopping-cart/cart-items/{shoppingCartItemId} - delete item from shopping cart


### orders (authenticated)

* GET - /api/v1/users/{userId}/orders - get all orders from user
* GET - /api/v1/users/{userId}/orders/{orderId} - get order by id
* POST - /api/v1/users/{userId}/orders - create new order (CUSTOMER roles)
* DELETE - /api/v1/users/{userId}/orders/{orderId} - delete order (CUSTOMER roles)
* GET - /api/v1/users/{userId}/orders/{orderId}/items - get items from order

### item-levels (authenticated)

* GET - /api/v1/item-levels - get all item-levels
* GET - /api/v1/item-levels/{itemLevelId} - get item level by id
* GET - /api/v1/item-levels/items-count - number of levels

### items (authenticated)

* GET - /api/v1/items - get all items
* GET - /api/v1/items/{itemId} - get item by id
* POST - /api/v1/items - add new item (ADMIN roles)
* POST - /api/v1/items/{itemId}/upload-image - upload item image  (ADMIN ROLES)
* PUT - /api/v1/items/{itemId} - update item (ADMIN roles) (ADMIN ROLES)
* PUT - /api/v1/items/{itemId}/update-image - change item image (ADMIN ROLES)
* DELETE - /api/v1/items/{itemId} - delete item (ADMIN ROLES)
* DELETE - /api/v1/items/{itemId}/delete-image (ADMIN ROLES)
* GET - /api/v1/items/{itemId}/category - get item category
* GET - /api/v1/items/{itemId}/item-level - get item level
* GET - /api/v1/items/top-selling - get top selling items

### item-categries (authenticated)

* GET - /api/v1/categories - get all item categories
* GET - /api/v1/categories/{itemCategoryId} - get item category by id
* POST - /api/v1/categories - create new item category (ADMIN roles)
* PUT - /api/v1/categories/{itemCategoryId} - update item category (ADMIN roles)
* DELETE - /api/v1/categories/{itemCategoryId} - delete item category
* GET - /api/v1/categories/{itemCategoryId}/items - get all item category items
* GET - /api/v1/categories/{itemCategoryId}/items-count - number of items in category

