module.exports = {
  'Enter Item': function(browser) {
    browser
      .url('localhost:3000')
      .waitForElementVisible('body', 1000)
      //.waitForElementVisible('#search-btn.btn.btn-info.btn-lg', 1000)
      .setValue('#search-input.form-control.input-lg', 'apple')
   },

   'Search' : function(browser) {
   	 browser
   	   .click('#search-btn.btn.btn-info.btn-lg')
   	   .pause(1000)
   	   .assert.visible('ul.products > li')
   	   .end();
   }
};