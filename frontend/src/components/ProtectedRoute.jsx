import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

/** Chua dang nhap -> /login. Sai vai tro -> ve trang chu. Dung lam layout route (Outlet). */
export default function ProtectedRoute({ roles }) {
  const { user } = useAuth();
  const location = useLocation();

  if (!user) return <Navigate to="/login" replace state={{ from: location }} />;
  if (roles && !roles.includes(user.vaiTro)) return <Navigate to="/" replace />;
  return <Outlet />;
}
