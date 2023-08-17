/* global order_id, payement_id */

const menu_toggle = document.querySelector('.menu-toggle');
const sidebar = document.querySelector('.sidebar');

menu_toggle.addEventListener('click', () => {
    menu_toggle.classList.toggle('is-active');
    sidebar.classList.toggle('is-active');
});

const search = () => {
    console.log("searching...");
    let query = $("#search").val();


    if (query === '') {
        $(".search-result").hide();
    } else {
        console.log(query);


        let url = `http://localhost:9192/search/${query}`;

        fetch(url).then((response) => {
            return response.json();
        })
                .then((data) => {
                    console.log(data);
                    let text = `<div class="list-group">`;
                    data.forEach((element) => {
                        text += `<a href='/user/view/${element.cid}' class="list-group-item list-group-item-action"> ${element.name} </a>`;
                    });
                    text += `</div>`;

                    $(".search-result").html(text);
                    $(".search-result").show();
                });
    }
};

const paymentorder = () => {
    console.log("payment started");
    let amount = $("#amount").val();

    console.log(amount);
    if (amount === "" || amount === null)
    {
       
        swal({
            className:".swal-modal",
            
                    title: "Oh No!!!!",
                    text: "Amount is required...!",
                    icon: "error",
                    button: "Oh No!!!"
});

        return;
    }

    $.ajax({
        url: '/user/paymentorder',
        data: JSON.stringify({amount: amount, info: 'order_request'}),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
            console.log(response);
            if (response.status === 'created')
            {
                // open payment form
                let options = {
                    key: 'rzp_test_qMQL538IvidV7n',
                    amount: response.amount,
                    currency: 'INR',
                    name: 'Smart Contact manager',
                    description: 'Donation',
                    image: 'https://logos-world.net/wp-content/uploads/2021/04/Smart-Logo.png',
                    order_id: response.id,
                    handler: function (response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        console.log('payment successfully!!!');
                        
                         updatePaymentOnserver(
                                response.razorpay_payment_id,
                                response.razorpay_order_id,
                                "paid"
                                );

                    },
                    prefill: {
                        "name": "",
                        "email": "",
                        "contact": ""
                    },
                    notes: {
                        "address": "Razorpay Integration - Smart contactor"
                    },
                    theme: {
                        "color": "#3399cc"
                    }
                };
                
                let rzp = new Razorpay(options);
                
                rzp.on("payment.failed",function (response){
                    console.log(response.error.code);
                    
                    swal({
                    title: "Oh No!!!!",
                    text: "Payment Failed...!",
                    icon: "error",
                    button: "Oh No!!!"
});
                    
                });
                
                
                rzp.open();
            }
        },
        error: function (error) {
            console.log(error);
            alert("something went wrong...");
        }

    });
};


function updatePaymentOnserver(payement_id,order_id,status)
{
    $.ajax({
         url: '/user/updateorder',
        data: JSON.stringify({
            payment_id: payement_id, 
            order_id:order_id,
            status:status
        
        }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success:function (response) {
            swal("Good job!", "Payment Successfully!!!", "success");
        },
        error:function (error) {
            
            swal({
                    title: "Failed!!!",
                    text: "Your Payment is Successful But Not yet updated on server!!!",
                    icon: "error",
                    button: "OK!!!"
});
        }
    });
}
