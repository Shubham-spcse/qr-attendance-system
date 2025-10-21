/**
 * Form Validation JavaScript
 * Handles client-side form validation
 */

document.addEventListener('DOMContentLoaded', function() {
    console.log('Validation.js initialized');
    initializeFormValidation();
});

/**
 * Initialize form validation
 */
function initializeFormValidation() {
    // Get all forms that need validation
    const forms = document.querySelectorAll('.needs-validation');

    Array.from(forms).forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }

            form.classList.add('was-validated');
        }, false);
    });

    // Initialize custom validators
    initializeEmailValidation();
    initializePasswordValidation();
    initializePhoneValidation();
    initializeRollNumberValidation();
}

/**
 * Email validation
 */
function initializeEmailValidation() {
    const emailInputs = document.querySelectorAll('input[type="email"]');

    emailInputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateEmail(this);
        });
    });
}

/**
 * Validate email format
 */
function validateEmail(input) {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailPattern.test(input.value);

    if (!isValid && input.value.length > 0) {
        input.setCustomValidity('Please enter a valid email address');
        showFieldError(input, 'Invalid email format');
    } else {
        input.setCustomValidity('');
        clearFieldError(input);
    }

    return isValid;
}

/**
 * Password validation
 */
function initializePasswordValidation() {
    const passwordInputs = document.querySelectorAll('input[type="password"][name="password"]');
    const confirmInputs = document.querySelectorAll('input[type="password"][name="confirmPassword"]');

    passwordInputs.forEach(input => {
        input.addEventListener('input', function() {
            validatePasswordStrength(this);
        });
    });

    confirmInputs.forEach(input => {
        input.addEventListener('input', function() {
            validatePasswordMatch(this);
        });
    });
}

/**
 * Validate password strength
 */
function validatePasswordStrength(input) {
    const password = input.value;
    const strengthBar = document.getElementById('password-strength-bar');
    const strengthText = document.getElementById('password-strength-text');

    if (!password) {
        if (strengthBar) strengthBar.style.width = '0%';
        if (strengthText) strengthText.textContent = '';
        return;
    }

    let strength = 0;
    let feedback = [];

    // Length check
    if (password.length >= 8) strength += 25;
    else feedback.push('At least 8 characters');

    // Uppercase check
    if (/[A-Z]/.test(password)) strength += 25;
    else feedback.push('One uppercase letter');

    // Lowercase check
    if (/[a-z]/.test(password)) strength += 25;
    else feedback.push('One lowercase letter');

    // Number check
    if (/[0-9]/.test(password)) strength += 25;
    else feedback.push('One number');

    // Update UI
    if (strengthBar) {
        strengthBar.style.width = strength + '%';
        strengthBar.className = 'progress-bar';

        if (strength < 50) {
            strengthBar.classList.add('bg-danger');
        } else if (strength < 75) {
            strengthBar.classList.add('bg-warning');
        } else {
            strengthBar.classList.add('bg-success');
        }
    }

    if (strengthText) {
        if (feedback.length > 0) {
            strengthText.textContent = 'Needs: ' + feedback.join(', ');
            strengthText.className = 'text-muted small';
        } else {
            strengthText.textContent = 'Strong password!';
            strengthText.className = 'text-success small';
        }
    }
}

/**
 * Validate password match
 */
function validatePasswordMatch(confirmInput) {
    const passwordInput = document.querySelector('input[type="password"][name="password"]');

    if (!passwordInput) return;

    const password = passwordInput.value;
    const confirm = confirmInput.value;

    if (confirm && password !== confirm) {
        confirmInput.setCustomValidity('Passwords do not match');
        showFieldError(confirmInput, 'Passwords do not match');
    } else {
        confirmInput.setCustomValidity('');
        clearFieldError(confirmInput);
    }
}

/**
 * Phone validation
 */
function initializePhoneValidation() {
    const phoneInputs = document.querySelectorAll('input[type="tel"]');

    phoneInputs.forEach(input => {
        input.addEventListener('input', function() {
            // Remove non-numeric characters
            this.value = this.value.replace(/[^0-9]/g, '');

            // Validate length
            if (this.value.length > 0 && this.value.length !== 10) {
                this.setCustomValidity('Phone number must be 10 digits');
                showFieldError(this, '10 digits required');
            } else {
                this.setCustomValidity('');
                clearFieldError(this);
            }
        });
    });
}

/**
 * Roll number validation
 */
function initializeRollNumberValidation() {
    const rollInputs = document.querySelectorAll('input[name="rollNumber"]');

    rollInputs.forEach(input => {
        input.addEventListener('input', function() {
            // Convert to uppercase
            this.value = this.value.toUpperCase();

            // Validate format: YYYYDDDNNN (e.g., 2023CSE001)
            const pattern = /^[0-9]{4}[A-Z]{3}[0-9]{3}$/;

            if (this.value.length > 0 && !pattern.test(this.value)) {
                this.setCustomValidity('Format: YYYYDDDNNN (e.g., 2023CSE001)');
                showFieldError(this, 'Invalid format');
            } else {
                this.setCustomValidity('');
                clearFieldError(this);
            }
        });
    });
}

/**
 * Show field error
 */
function showFieldError(input, message) {
    let errorDiv = input.nextElementSibling;

    if (!errorDiv || !errorDiv.classList.contains('invalid-feedback')) {
        errorDiv = document.createElement('div');
        errorDiv.className = 'invalid-feedback';
        input.parentNode.insertBefore(errorDiv, input.nextSibling);
    }

    errorDiv.textContent = message;
    input.classList.add('is-invalid');
}

/**
 * Clear field error
 */
function clearFieldError(input) {
    const errorDiv = input.nextElementSibling;

    if (errorDiv && errorDiv.classList.contains('invalid-feedback')) {
        errorDiv.textContent = '';
    }

    input.classList.remove('is-invalid');
}

/**
 * Validate entire form
 */
function validateForm(formId) {
    const form = document.getElementById(formId);

    if (!form) return false;

    const isValid = form.checkValidity();
    form.classList.add('was-validated');

    return isValid;
}

// Export functions
window.ValidationJS = {
    validateEmail,
    validatePasswordStrength,
    validatePasswordMatch,
    validateForm,
    showFieldError,
    clearFieldError
};
