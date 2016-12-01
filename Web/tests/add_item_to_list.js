/**
 * End-to-end test for whether a user can visit the website, search for an item
 * and add the item to a list.
 */

module.exports = {
  'Enter Item': function(browser) {
    browser
      .url('http://checkedout.ca/')
      .waitForElementVisible('body', 1000)
      .setValue('#search-input.form-control.input-lg', 'apple')
   },

   'Search' : function(browser) {
   	 browser
   	   .click('#search-btn.btn.btn-info.btn-lg')
   	   .pause(1000)
   	   .assert.visible('ul.products')
       .pause(500)
       .assert.elementPresent('#item0')
   },

   'Add Item to List': function(browser) {
     browser
       .click('#item0')
       .pause(1000)
       // need to check if the item was actually added to the list
       .end()
   }
};