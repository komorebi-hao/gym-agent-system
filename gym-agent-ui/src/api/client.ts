import axios from 'axios'

const api = axios.create({
  withCredentials: true
})

export function postForm(url: string, data: Record<string, any>) {
  const body = new URLSearchParams()
  Object.keys(data || {}).forEach((k) => {
    const v = data[k]
    if (v !== undefined && v !== null) body.append(k, String(v))
  })
  return api.post(url, body, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
    }
  })
}

export default api
