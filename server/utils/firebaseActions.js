export const sendNotification = async (to, title, body) => {
  const url = 'https://fcm.googleapis.com/fcm/send';
  const notification = { title, body };
  const bodyJson = { to, notification };
  const Authorization =
    'key=AAAAYTeLLcg:APA91bHylnquW1Ayh7D9_PgVteJxMAK_WrCr2e9k_ivg0vgqK0rtIexchne_Wk9fbEbSKQp14iVwdenBFcEnQg9ELiGvHT8O8l2uu5Kpfpxrz2BAfpPG40xRKIfiXE_cscplzQKpajeb';
  const headersJson = {
    Authorization,
    'Content-Type': 'application/json',
  };

  const resp = await fetch(url, {
    headers: headersJson,
    method: 'POST',
    body: JSON.stringify(bodyJson),
  });
  console.log(resp.status);
};
