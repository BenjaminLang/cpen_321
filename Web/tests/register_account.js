/**
 * End-to-end test for whether a user can visit the website and register for an account
 */
var name = 'testuser';
var email = 'testemail6@hotmail.com';
var password = 'TESTPASSWORD1';

module.exports = {
  'Go to Registration': function(browser) {
    browser
      .url('https://www.checkedout.ca/')
      .waitForElementVisible('body', 1000)
      .click('#account')
      .pause(500)
      .click('#register')
      .pause(500)
      .waitForElementVisible('.Register', 1000)
   },

  'Enter account information' : function(browser) {
    browser
      .setValue('input[name="name"]', name)
      .setValue('input[name="email"]', email)
      .setValue('input[name="email_verify"]', email)
      .setValue('input[name="password"]', password)
      .setValue('input[name="password_verify"]', password)
      .submit('input[type="submit"]')
      .pause(2000)
      .assert.containsText('.greeting', 'Hello, ' + name)
  },

  /*
  'Delete account' : function(browser) {
    browser

      .end()
  }
  */
};