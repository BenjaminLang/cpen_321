/**
 * End-to-end test for whether a user can visit the website, register for an account, logout, 
 * and attempt to register for another account with the same email, but fail.
 */
var name = 'testuser';
var email = 'testemail1@hotmail.com';
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
      .click('input[type="submit"]')
      .pause(1000)
      .assert.containsText('.greeting', 'Hello, ' + name)
  },

  'Logout and attempt to register same account' : function(browser) {
    browser
      .click('#account')
      .pause(500)
      .click('#logout')
      .pause(500)
      .assert.elementNotPresent('.greeting')
      .click('#account')
      .pause(500)
      .click('#register')
      .pause(500)
      .setValue('input[name="name"]', name)
      .setValue('input[name="email"]', email)
      .setValue('input[name="email_verify"]', email)
      .setValue('input[name="password"]', password)
      .setValue('input[name="password_verify"]', password)
      .click('input[type="submit"]')
      .pause(1000)
      .assert.containsText('h4', 'That email is already in use.')
  },

  'Login' : function(browser) {
    browser
      .click('#account')
      .pause(500)
      .click('#login')
      .pause(500)
      .setValue('input[name="email"]', email)
      .setValue('input[name="password"]', password)
      .click('input[type="submit"]')
      .pause(1000)
      .assert.containsText('.greeting', 'Hello, ' + name)
  },

  // Delete account so we can reuse this test
  'Go to Delete Account' : function(browser) {
    browser
      .click('#account')
      .pause(500)
      .click('#delete_acc')
      .pause(500)
      .waitForElementVisible('.Delete_Acc', 1000)
  },

  'Delete Account' : function(browser) {
    browser
      .setValue('input[name="password"]', password)
      .click('input[type="submit"]')
      .pause(1000)
      .assert.elementNotPresent('.greeting')
      .end();
  }
  
};