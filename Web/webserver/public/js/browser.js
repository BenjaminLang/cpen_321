var socket = io();

/**
 * Ways to enter a search result
 */
$('#search-btn').click(() => {
	socket.emit('search_request', $('#search-input').val());
	// View the search results in a different page
	window.location.href = '/item_searched';
});

$('#search-input').keyup((event) => {
	// Enter key
	if (event.keyCode == 13) {
		$('#search-btn').click();
	}
});

socket.on('search response', function(response) {
	console.log(response);
});

/*$("button").click( function() {
    $.getJSON("json_website_data.json", function(data) { 
	    $.each(data, function(key, value){ 
		    $("ul").append("<li>"+value.name+"</li>");
		    $("ul").append("<li>"+value.age+"</li>");
			$("ul").append("<li>"+value.company+"</li>");
		});
	});
});
*/




//All Javascript for Cart Functionality Below: 





/** The constructor function both initializes the top-level element 
 *  that wraps our DOM’s entire structure and invokes the initialization method.
 */
 
(function( $ ) {
	$.Shop = function( element ) {
		this.$element = $( element ); // top-level element
		this.init();
	};

	$.Shop.prototype = {
		init: function() {
			// initializes properties and methods
			
			// Properties
			this.cartPrefix = "cartprefix-"; // prefix string to be prepended to the cart's name in session storage
			this.cartName = this.cartPrefix + "cart"; // cart's name in session storage
			this.total = this.cartPrefix + "total"; // total key in the session storage
			this.storage = sessionStorage; // shortcut to sessionStorage object


			this.$formAddToCart = this.$element.find( "form.add-to-cart" ); // forms for adding items to the cart
			this.$formCart = this.$element.find( "#shopping-cart" ); // Shopping cart form
			this.$subTotal = this.$element.find( "#stotal" ); // element that displays the subtotal charges
			this.$shoppingCartActions = this.$element.find( "#shopping-cart-actions" ); // cart actions links
			this.$updateCartBtn = this.$shoppingCartActions.find( "#update-cart" ); // update cart button
			this.$emptyCartBtn = this.$shoppingCartActions.find( "#empty-cart" ); // empty cart button


			this.currency = "&CAD;"; // HTML entity of the currency to be displayed in layout
			this.currencyString = "$"; // currency symbol as text string
			

			// object containing patterns for form validation
			this.requiredFields = {
				expression: {
					value: /^([w-.]+)@((?:[w]+.)+)([a-z]){2,4}$/
				},

				str: {
					value: ""
				}

			};
			
			
			
			
			// Creates the cart keys in session storage

            createCart: function() {
            	if( this.storage.getItem( this.cartName ) == null ) {
            
            		var cart = {};
            		cart.items = [];
            
            		this.storage.setItem( this.cartName, this._toJSONString( cart ) );
            		this.storage.setItem( this.shippingRates, "0" );
            		this.storage.setItem( this.total, "0" );
            	}
            }
			
			
			
			
			
			// Adds items to shopping cart

            handleAddToCartForm: function() {
            	var self = this;
            	self.$formAddToCart.each(function() {
            		var $form = $( this );
            		var $product = $form.parent();
            		var price = self._convertString( $product.data( "price" ) );
            		var name =  $product.data( "name" );
            
            		$form.on( "submit", function() {
            			var qty = self._convertString( $form.find( ".qty" ).val() );
            			var subTotal = qty * price;
            			var total = self._convertString( self.storage.getItem( self.total ) );
            			var sTotal = total + subTotal;
            			self.storage.setItem( self.total, sTotal );
            			self._addToCart({
            				product: name,
            				price: price,
            				qty: qty
            			});
            			var shipping = self._convertString( self.storage.getItem( self.shippingRates ) );
            			var shippingRates = self._calculateShipping( qty );
            			var totalShipping = shipping + shippingRates;
            
            			self.storage.setItem( self.shippingRates, totalShipping );
            		});
            	});
            }
			
			
			
			
			displayCart: function() {
			   	if( this.$formCart.length ) {
            		var cart = this._toJSONObject( this.storage.getItem( this.cartName ) );
            		var items = cart.items;
            		var $tableCart = this.$formCart.find( ".shopping-cart" );
            		var $tableCartBody = $tableCart.find( "tbody" );
			
					
					for ( var i = 0; i < items.length; ++i) {
            			var item = items[i];
            			var product = item.product;
            			var price = this.currency + " " + item.price;
            			var qty = item.qty;
            			var html = "<tr><td class='pname'>" + product + "</td>" + "<td class='pqty'><input type='text' value='" + qty + "' class='qty'/></td>" + "<td class='pprice'>" + price + "</td></tr>";
            
            			$tableCartBody.html( $tableCartBody.html() + html );
            		}
					
					
					var total = this.storage.getItem( this.total );
              		this.$subTotal[0].innerHTML = this.currency + " " + total;
                } 
			}
		
		
			
			
			
			
			
			// Updates the cart

            updateCart: function() {
            		var self = this;
            	if( self.$updateCartBtn.length ) {
            		self.$updateCartBtn.on( "click", function() {
            			var $rows = self.$formCart.find( "tbody tr" );
            			var cart = self.storage.getItem( self.cartName );
            			var shippingRates = self.storage.getItem( self.shippingRates );
            			var total = self.storage.getItem( self.total );
            
            			var updatedTotal = 0;
            			var totalQty = 0;
            			var updatedCart = {};
            			updatedCart.items = [];
            
            			$rows.each(function() {
            				var $row = $( this );
            				var pname = $.trim( $row.find( ".pname" ).text() );
            				var pqty = self._convertString( $row.find( ".pqty > .qty" ).val() );
            				var pprice = self._convertString( self._extractPrice( $row.find( ".pprice" ) ) );
            
            				var cartObj = {
            					product: pname,
            					price: pprice,
            					qty: pqty
            				};
            
            				updatedCart.items.push( cartObj );
            
            				var subTotal = pqty * pprice;
            				updatedTotal += subTotal;
            				totalQty += pqty;
            			});
            
            			self.storage.setItem( self.total, self._convertNumber( updatedTotal ) );
            			self.storage.setItem( self.shippingRates, self._convertNumber( self._calculateShipping( totalQty ) ) );
            			self.storage.setItem( self.cartName, self._toJSONString( updatedCart ) );
            
            		});
            	}
            }
			
			
			
			
			// Empties the cart by calling the _emptyCart() method
            // @see $.Shop._emptyCart()
            
            emptyCart: function() {
            	var self = this;
            	if( self.$emptyCartBtn.length ) {
            		self.$emptyCartBtn.on( "click", function() {
            			self._emptyCart();
            		});
            	}
            }
		}
		
		
		
		//Private Methods
		
		_emptyCart: function() {
			this.storage.clear();
		}
		
		
		
		
		
		/* Format a number by decimal places
         * @param num Number the number to be formatted
         * @param places Number the decimal places
         * @returns n Number the formatted number
        */
        
        _formatNumber: function( num, places ) {
        	var n = num.toFixed( places );
        	return n;
        }
		
		
		
		
		/* Extract the numeric portion from a string
         * @param element Object the jQuery element that contains the relevant string
         * @returns price String the numeric string
         */
        
        _extractPrice: function( element ) {
        	var self = this;
        	var text = element.text();
        	var price = text.replace( self.currencyString, "" ).replace( " ", "" );
        	return price;
        }
		
		
		
		
		
		
		/* Converts a numeric string into a number
         * @param numStr String the numeric string to be converted
         * @returns num Number the number, or false if the string cannot be converted
         */
        
        _convertString: function( numStr ) {
        	var num;
        	if( /^[-+]?[0-9]+.[0-9]+$/.test( numStr ) ) {
        		num = parseFloat( numStr );
        	} else if( /^d+$/.test( numStr ) ) {
        		num = parseInt( numStr );
        	} else {
        		num = Number( numStr );
        	}
        
        	if( !isNaN( num ) ) {
        		return num;
        	} else {
        		console.warn( numStr + " cannot be converted into a number" );
        		return false;
        	}
        },
        
        /* Converts a number to a string
         * @param n Number the number to be converted
         * @returns str String the string returned
         */
        
        _convertNumber: function( n ) {
        	var str = n.toString();
        	return str;
        }
		
		
		
		
		
		
		
		/* Converts a JSON string to a JavaScript object
         * @param str String the JSON string
         * @returns obj Object the JavaScript object
         */
        
        _toJSONObject: function( str ) {
        	var obj = JSON.parse( str );
        	return obj;
        },
        
        /* Converts a JavaScript object to a JSON string
         * @param obj Object the JavaScript object
         * @returns str String the JSON string
         */
        
        
        _toJSONString: function( obj ) {
        	var str = JSON.stringify( obj );
        	return str;
        }
		
		
		
		
		
		
		
		
		
		
		/* Add an object to the cart as a JSON string
         * @param values Object the object to be added to the cart
         * @returns void
         */
        
        
        _addToCart: function( values ) {
        	var cart = this.storage.getItem( this.cartName );
        	var cartObject = this._toJSONObject( cart );
        	var cartCopy = cartObject;
        	var items = cartCopy.items;
        	items.push( values );
        
        	this.storage.setItem( this.cartName, this._toJSONString( cartCopy ) );
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	};

	$(function() {
		var shop = new $.Shop( "#site" ); // object's instance
	});

})( jQuery );







		



