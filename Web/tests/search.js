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
   	  .end();
   }
};