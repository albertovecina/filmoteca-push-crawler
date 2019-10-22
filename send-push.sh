#/bin/bash
registrationIds='['
for i; do
    registrationIds+='"'$i
    registrationIds+='",'
done
registrationIds=${registrationIds%?}]
body='{"registration_ids":'$registrationIds',"notification":{"title":"","title_loc_key":"notification_title_normal","body":"","body_loc_key":"notification_message_new_movies","icon":"ic_notification","sound":"default"}}'
echo $body
curl -X POST \
  https://fcm.googleapis.com/fcm/send \
  -H 'Authorization: key=AIzaSyB9wjaM4he7bxpWpk1oBUIR6upi55tGANY' \
  -H 'Content-Type: application/json' \
	-d `echo $body`
