var htmlString;

function render() {
  $('.modal-body').html(htmlString);
}

function resetLocalStorage() {
    localStorage.cart = JSON.stringify([]);
    render();
    location.reload();
}

//showCart Functions
function showCart() {
  var modalBody = $('.modal-body');
  htmlString = '';
  var cartItems = getAllItems();
  //console.log(cartItems);
  cartItems.forEach(function(item){
    console.log(item);
    htmlString += '<div class="media">' + 
    '<a href="#">' + 
      '<img class="media-object" src="' + item.Image + '" alt="...">' +
    '</a>' +
    '</div>' +
    '<div class="media-body">' +
      '<h4 class="media-heading">' + item.Name + '</h4>' + 
      '<p class="itemPrice">' + item.Price + '</p>' + 
      '<p class="itemStore">' + item.Store + '</p>' +
    '</div>' + 
    '</div>';
    render();
  });       
} 
    
function showCartButtonClicked() {
    console.log("44444");
    showCart();
}

//addToCart Functions
function addToLocalStorage(obj) {
 // console.log(obj);
  //  console.log("3");
  var data = JSON.parse(localStorage.cart);
  
//  console.log("4");
  data.push(obj);
 // console.log("5");
  localStorage.cart = JSON.stringify(data);
 // console.log("6");
// console.log(localStorage.cart);
}

function getAllItems() {
  return JSON.parse(localStorage.cart);
}
    
function createObjectForItem(item) {
   // console.log("2");
  var itemName = item.siblings("h3").text();
  var itemImageURL =  item.parent().siblings("img").attr("src");
  var itemPrice =  item.siblings("p:eq(0)").text();
  var itemStore =  item.siblings("p:eq(1)").text();
  var itemQuantity =  1;
  
  var itemObject = {};
  itemObject = {
          Name: itemName,
          Image: itemImageURL,
          Price: itemPrice,
          Store: itemStore,
          Quantity : itemQuantity
      };
  return itemObject;
}

function addToCartButtonClicked(element) {
   // console.log("addClicked");
    addToLocalStorage(createObjectForItem(element));
}

/* function itemAlreadyInCart(itemObj){
  allItems = getAllItems();
  var i = 0;
  for (itemObjj in allItems){
    if(itemOjj.Name==itemObj.Name){
       return i;
    } i++;
} */

$(document).click(function (e){
  
  //e.PreventDefault();
  e.stopPropagation();
  //console.log("something clicked");
  var tag  = $(event.target);
  //console.log(tag);
  if(tag.hasClass('addToCartButton')){ 
  /* //enter if clicked HTML element is Add To Cart Button
    var newItemObject = createObjectForItem(tag); //create an Cartobject for that item
    console.log(newItemObject);
    var index = itemAlreadyInCart(newItemObject.Name); //use that CartObject for comparison with localStorage.cart(array) object items
    console.log(index);
    
    if(index != -1){
      //item does already exist
      localStorage.cart[index].Quantity += 1;
    }
    else{ */
      addToCartButtonClicked(tag);
      /* } */
  }
  if(tag.hasClass('showCartButton')){
    showCartButtonClicked();
  }
  var cart = getAllItems();
  //console.log(cart);
});
