<?php
require 'vendor/autoload.php';
if(isset($_POST['authKey']) && ($_POST['authKey'] == "abc")){
    $stripe = new \Stripe\StripeClient('sk_test_51Pqw0kP5LTAWsdBPmzI8XhFiqL1EjMK83gTu0QLr771fQ8bvqs3AUg47BA7xU3fIu5BBCZWjRI0ViblO9I5Ms3qf00uDCytBDo');

    // Use an existing Customer ID if this is a returning customer.
    $customer = $stripe->customers->create(
        [
            'name' => "User",
            'address' => [
                'line1' => "AddressABC",
            ]
        ]);
    $ephemeralKey = $stripe->ephemeralKeys->create([
      'customer' => $customer->id,
    ], [
      'stripe_version' => '2024-06-20',
    ]);

    $paymentIntent = $stripe->paymentIntents->create([
      'amount' => 100 * 1299,
      'currency' => 'inr',
      'description' => "Payment for Visa Application",
      'customer' => $customer->id,
      // In the latest version of the API, specifying the `automatic_payment_methods` parameter
      // is optional because Stripe enables its functionality by default.
      'automatic_payment_methods' => [
        'enabled' => 'true',
      ],
    ]);

    echo json_encode(
      [
        'paymentIntent' => $paymentIntent->client_secret,
        'ephemeralKey' => $ephemeralKey->secret,
        'customer' => $customer->id,
        'publishableKey' => 'pk_test_51Pqw0kP5LTAWsdBPBVq6YtfB9X76nx3OJUwGKfdYFQ8pUIzlERsJBxu3XN1kKFj7XFWiW17EHq0kGMqE5KN7sq1W00D3mr5JR6'
      ]
    );
    http_response_code(200);
} else
    echo "NA";