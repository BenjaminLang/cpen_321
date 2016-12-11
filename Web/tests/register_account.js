/**
 * End-to-end test for whether a user can visit the website and register for an account.
 * Tests if server refuses an attempt to make an account with the same email.
 */
var name = 'testuser';
var email = 'testemail13@hotmail.com';
var password = 'TESTPASSWORD1';
var verification_code = 666666;

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
      .setValue('input[name="test"]', 'True')
      // Register
      .click('input[type="submit"]')
      .pause(1000)
      // Automatically redirect to login page
      .waitForElementVisible('#login-page')
  },

  'Verify Email' : function(browser) {
    browser
      .click('li#verify')
      .pause(500)
      .waitForElementVisible('.Verify', 1000)
      .setValue('input[name="email"]', email)
      .setValue('input[name="verify_num"]', verification_code)
      .click('input[type="submit"]')
      .pause(1000)
      .assert.containsText('h4', 'Email successfully verified.')
  },

  'Login' : function(browser) {
    browser
      .click('li#login') // go back to login page
      .pause(500)
      .setValue('input[name="email"]', email)
      .setValue('input[name="password"]', password)
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

  'Login again' : function(browser) {
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
      // There shouldn't be a "Hello ____" on the home page anymore
      .assert.elementNotPresent('.greeting')
      .end();
  }
  
};