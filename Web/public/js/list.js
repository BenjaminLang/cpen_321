var htmlString;

function render() {
  $('.modal-body').html(htmlString);
}

function resetLocalStorageIndex() {
    localStorage.cart = JSON.stringify([]);
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
      '<img class="media-object" src="' + item.image + '" alt="...">' +
    '</a>' +
    '</div>' +
    '<div class="media-body">' +
      '<h4 class="media-heading">' + item.name + '</h4>' + 
      '<p class="itemPrice">' + item.price + '</p>' + 
      '<p class="itemStore">' + item.store + '</p>' +
    '</div>' + 
    '</div>';
    render();
  });       
} 
    
function showCartButtonClicked() {
    showCart();
}

//addToCart Functions
function addToLocalStorage(obj) {
  var data = JSON.parse(localStorage.cart);
  
  data.push(obj);

  localStorage.cart = JSON.stringify(data);
}

function getAllItems() {
  return JSON.parse(localStorage.cart);
}
    
function createObjectForItem(item) {
  var itemName = item.siblings("h3").text();
  var itemImageURL =  item.parent().siblings("img").attr("src");
  var itemPrice =  item.siblings("p:eq(0)").text();
  var itemStore =  item.siblings("p:eq(1)").text();
  var itemQuantity =  '1';
  
  var itemObject = {};
  itemObject = {
          name: itemName,
          image: itemImageURL,
          price: itemPrice,
          store: itemStore,
          quantity : itemQuantity
      };
  return itemObject;
}

function addToCartButtonClicked(element) {
    addToLocalStorage(createObjectForItem(element));
}


$(document).click(function (e){
  e.stopPropagation();

  var tag  = $(event.target);

  if(tag.hasClass('addToCartButton')){ 
    addToCartButtonClicked(tag);
  }
  if(tag.hasClass('showCartButton')){
    showCartButtonClicked();
  }
  var cart = getAllItems();

});




// With JQuery
$("#ex7").slider();

// Without JQuery
//var slider = new Slider("#ex7");

$("#ex7-enabled").click(function() {
	if(this.checked) {
		// With JQuery
		$("#ex7").slider("enable");

		// Without JQuery
		//slider.enable();
	}
	else {
		// With JQuery
		$("#ex7").slider("disable");

		// Without JQuery
		//slider.disable();
	}
});


