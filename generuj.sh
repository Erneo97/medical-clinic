#!/usr/bin/env bash
d=2030-01-22

for p in 1000-1030 1015-1045 1030-1100 0930-1000 1005-1035 1000-1020; do
  od=${p%-*}
  do_=${p#*-}
  printf '{"doctorId":2,"startsAt":"%sT%s:%s:00","endsAt":"%sT%s:%s:00"}\n' \
    "$d" "${od:0:2}" "${od:2:2}" "$d" "${do_:0:2}" "${do_:2:2}" > "s-$p.json"
done

# sesja z przeszłości (do testu reguły odrzucającej daty z przeszłości)
printf '{"trainerId":1,"startsAt":"2020-01-06T10:00:00","endsAt":"2020-01-06T10:30:00"}\n' > s-2020.json